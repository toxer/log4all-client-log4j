package net.log4all.log4j;

import java.io.FileNotFoundException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

import org.apache.log4j.xml.DOMConfigurator;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Created by igor on 05/06/14.
 */
public class Log4AllAppenderTest {
    private Logger logger;
    private int seq=0;
    
    @BeforeClass
    public void initLog4j(){
        DOMConfigurator.configure(getClass().getClassLoader().getResource("log4j.xml"));
        logger=Logger.getLogger(getClass());
    }

    @Test(threadPoolSize = 10, invocationCount = 100)
    public void longErrorTest(){
        try{
            throw new Exception("test exception",new FileNotFoundException("test filenotfoundException"));
        }catch(Exception e) {
            logger.error("#type:longErrorTest  test message very long very long  very long  very long  very long  very long  very long " +
                    " very long  very long  very long  very long  very long  very long  very long " +
                    " very long  very long  very long  very long  very long  very long  very long  very long " +
                    " very long  very long  very long  very long  very long  very long  very long  very long " +
                    "#application:log4j-test #rand:"+new Date().getSeconds(),e);
        }
    }

    @Test(threadPoolSize = 10, invocationCount = 100)
    public void longWarnTest(){
        try{
            throw new Exception("#test:longWarnTest test exception",new FileNotFoundException("test filenotfoundException"));
        }catch(Exception e) {
            logger.warn("#type:long  test message very long very long  very long  very long  very long  very long  very long " +
                    " very long  very long  very long  very long  very long  very long  very long " +
                    " very long  very long  very long  very long  very long  very long  very long  very long " +
                    " very long  very long  very long  very long  very long  very long  very long  very long " +
                    "#application:log4j-test #rand:" + new Date().getSeconds(), e);
        }
    }

    @Test(invocationCount = 1000)
    public void sequenceTest() throws InterruptedException {
       logger.info("Sequence test #test:sequenceTest ##seq:"+seq);
       seq++;
    }

    @Test
    public void basicTest() throws InterruptedException {
        try{
            throw new Exception("test exception",new FileNotFoundException("test filenotfoundException"));
        }catch(Exception e) {
            logger.info("#type:basic  test message #application:log4j-test2",e);
        }
    }

    @Test
    public void nullMessageTest() throws InterruptedException {
        try{
            throw new Exception("test exception",new FileNotFoundException("test filenotfoundException"));
        }catch(Exception e) {
            logger.info(null,e);
        }
    }

     @Test
    public void mdcTest() throws InterruptedException {
        try{
            //remember to use escape sequences for keys: ##!
            MDC.put("##!chiave1", "valore1");
            MDC.put("##!chiave2", "valore2");
            throw new Exception("test exception",new FileNotFoundException("test filenotfoundException"));
        }catch(Exception e) {
            //TEST with message and MDC keys
            logger.info("MdcTest",e);
            // TEST with null message and MDC keys
             logger.info(null,e);
            MDC.remove("##!chiave1");
            MDC.remove("##!chiave2");
            //TEST without MDC keys
            logger.info("MdcTest2",e);
        }
         finally{
             MDC.remove("##!chiave1");
             MDC.remove("##!chiave2");

         }
    }
    
    @AfterClass
    public void waitThreads(){
    	System.out.println("Waiting 10 seconds..");
    	int sec=0;
		while(sec <10){
			try {
				Thread.sleep(1000);
				sec++;
			} catch (InterruptedException e) {
			
			}
		}
    }
}
