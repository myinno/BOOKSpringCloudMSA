//import reactor.core.publisher.Flux;
//import reactor.core.scheduler.Schedulers;
//
//public class Myinno01 {
//
//	public static void main(String[] args) {
//		Flux.range(1, 20)
//		.parallel(2) // 작업을 레일로 나누기만 함
//		.runOn(Schedulers.newParallel("PAR", 2))  // 각 레일을 병렬로 실행
//		.map(x -> {
//		    int sleepTime = nextSleepTime(x % 2 == 0 ? 50 : 100, x % 2 == 0 ? 150 : 300);
//		    logger.info("map1 {}, sleepTime {}", x, sleepTime);
//		    sleep(sleepTime);
//		    return String.format("%02d", x);
//		})
//		.subscribe(i -> logger.info("next {}", i) );
//	}
//
//}
