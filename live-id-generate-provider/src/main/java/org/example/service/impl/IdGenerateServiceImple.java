package org.example.service.impl;

import jakarta.annotation.Resource;
import org.example.bo.LocalSeqIdBO;
import org.example.bo.LocalUnSeqIdBO;
import org.example.dao.pojo.TIdGenerateConfig;
import org.example.service.IdGenerateService;
import org.example.service.TIdGenerateConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class IdGenerateServiceImple implements IdGenerateService, InitializingBean {


    @Resource
    private TIdGenerateConfigService tIdGenerateConfigService;

    private static Map<Integer, LocalSeqIdBO> localSeqIdBOMap = new HashMap<>();

    private static Map<Integer, LocalUnSeqIdBO> localUnSeqIdBOMap = new HashMap<>();


    private final static Logger log = LoggerFactory.getLogger(IdGenerateServiceImple.class);

    private final static double LIMIT_RATE = 0.75;

    private final static Map<Integer, Semaphore> localSeqSemaphoreMap = new HashMap<>();

    private final static Map<Integer, Semaphore> localUnSeqSemaphoreMap = new HashMap<>();

    private final static ThreadPoolExecutor updatePool = new ThreadPoolExecutor(1, 1, 1, TimeUnit.MICROSECONDS, new ArrayBlockingQueue<>(1), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r,"idgenerate-update-"+ThreadLocalRandom.current().ints(10));
        }
    },new ThreadPoolExecutor.AbortPolicy());

    @Override
    public Integer getSeqId(Integer code) {
        if (code == null) {
            log.error("业务id有误 {}", code);
            return null;
        }

        LocalSeqIdBO localSeqIdBO = localSeqIdBOMap.get(code);

        if (localSeqIdBO == null) {
           log.error("主键业务id不存在 - {}",code);
        }

        //当使用达到百分之七十五后，刷新
        if ((localSeqIdBO.getNextThrshold()-localSeqIdBO.getCurrentStart())*LIMIT_RATE<=localSeqIdBO.getCurrentid().longValue()-localSeqIdBO.getCurrentStart()) {
            Semaphore semaphore = localSeqSemaphoreMap.get(code);
            if (!syncUpdateAndReInitStart(code, semaphore)) return null;
        }


        AtomicInteger currentid = localSeqIdBO.getCurrentid();

        int andIncrement = currentid.getAndIncrement();

        if (andIncrement>localSeqIdBO.getNextThrshold()){
            log.error("业务段id超出上限");
        }

        return andIncrement;
    }

    @Override
    public Integer getUnSeqId(Integer code) {
        if (code == null) {
            log.error("业务id有误 {}", code);
            return null;
        }

        LocalUnSeqIdBO localUnSeqIdBO = localUnSeqIdBOMap.get(code);

        if (localUnSeqIdBO == null) {
            log.error("主键业务id不存在 - {}",code);
        }

        ConcurrentLinkedQueue<Integer> unSeqQueue = localUnSeqIdBO.getUnSeqQueue();

        if (unSeqQueue.size()==0){
            return null;
        }

        if (localUnSeqIdBO.getStep()*(1-LIMIT_RATE)>=unSeqQueue.size()) {
            Semaphore semaphore = localSeqSemaphoreMap.get(code);
            if (!syncUpdateAndReInitStart(code, semaphore)) return null;
        }

        return unSeqQueue.poll();

    }

    private boolean syncUpdateAndReInitStart(Integer code, Semaphore semaphore) {
        if (semaphore ==null){
            return false;
        }else {
            try {
                boolean tryAcquire = semaphore.tryAcquire();
                if (tryAcquire) {
                    updatePool.execute(new Runnable() {
                        @Override
                        public void run() {
                            log.info("开始更新业务主键区间 {}", code);
                            TIdGenerateConfig generateConfigServiceById = tIdGenerateConfigService.getById(code);
                            initSeqMapAndUpdate(generateConfigServiceById);
                            semaphore.release();
                        }
                    });
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<TIdGenerateConfig> tIdGenerateConfigList = tIdGenerateConfigService.list();

        for (TIdGenerateConfig tIdGenerateConfig : tIdGenerateConfigList) {
            initSeqMapAndUpdate(tIdGenerateConfig);
            if (tIdGenerateConfig.getIsSeq()==1) {
                localSeqSemaphoreMap.put(tIdGenerateConfig.getId(), new Semaphore(1));
            }else {
                localUnSeqSemaphoreMap.put(tIdGenerateConfig.getId(), new Semaphore(1));
            }
        }
    }

    private void initSeqMapAndUpdate(TIdGenerateConfig tIdGenerateConfig) {
        //先更新
        Integer id = tIdGenerateConfig.getId();
        Integer version = tIdGenerateConfig.getVersion();
        int updateResult = tIdGenerateConfigService.updateNewVersion(id, version);
        Integer isSeq = tIdGenerateConfig.getIsSeq();

        if (updateResult>0) {

            if (isSeq==1) {
                initSeqMap(tIdGenerateConfig, id);
            }else {
                initUnSeqMap(tIdGenerateConfig, id);
            }
        }else {
            //再次尝试更新
            for (int i = 0; i < 3; i++) {
                TIdGenerateConfig generateConfigServiceById = tIdGenerateConfigService.getById(id);
                updateResult = tIdGenerateConfigService.updateNewVersion(id,generateConfigServiceById.getVersion());
                isSeq= generateConfigServiceById.getIsSeq();
                if (updateResult>0) {
                    if (isSeq==1) {
                        initSeqMap(generateConfigServiceById, id);
                    }else {
                        initUnSeqMap(generateConfigServiceById, id);
                    }
                }
            }
            throw new RuntimeException("业务id更新失败");
        }
    }


    private static void initSeqMap(TIdGenerateConfig tIdGenerateConfig, Integer id) {
        Integer currentStart = tIdGenerateConfig.getCurrentStart() + tIdGenerateConfig.getStep();
        AtomicInteger atomicInteger = new AtomicInteger(currentStart);

        LocalSeqIdBO initlocalSeqIdBO = new LocalSeqIdBO();
        initlocalSeqIdBO.setCode(id);
        initlocalSeqIdBO.setCurrentid(atomicInteger);
        initlocalSeqIdBO.setCurrentStart(currentStart.longValue());
        initlocalSeqIdBO.setNextThrshold(tIdGenerateConfig.getNextThreshold().longValue() + tIdGenerateConfig.getStep());

        localSeqIdBOMap.put(id, initlocalSeqIdBO);
    }


    private static void initUnSeqMap(TIdGenerateConfig tIdGenerateConfig, Integer id) {
        Integer step = tIdGenerateConfig.getStep();
        Integer currentStart = tIdGenerateConfig.getCurrentStart() + step;
        List<Integer> idList = new ArrayList<>();
        for (Integer i = 0; i < step; i++) {
            idList.add(currentStart+i);
        }
        //打乱
        Collections.shuffle(idList);

        ConcurrentLinkedQueue<Integer> idUnseqQueue = new ConcurrentLinkedQueue<>();
        idUnseqQueue.addAll(idList);

        LocalUnSeqIdBO initlocalUnSeqIdBO = new LocalUnSeqIdBO();
        initlocalUnSeqIdBO.setCode(id);
        initlocalUnSeqIdBO.setUnSeqQueue(idUnseqQueue);
        initlocalUnSeqIdBO.setCurrentStart(currentStart.longValue());
        initlocalUnSeqIdBO.setNextThrshold(tIdGenerateConfig.getNextThreshold().longValue() + tIdGenerateConfig.getStep());
        localUnSeqIdBOMap.put(id,initlocalUnSeqIdBO);
    }
}
