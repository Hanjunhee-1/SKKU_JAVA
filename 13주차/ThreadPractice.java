import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ThreadPractice extends Thread {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(5);
		
		Producer p = new Producer("Producer", queue);
		p.start();
		Consumer c = new Consumer("Consumer", queue);
		c.start();
	}

}


class Producer extends Thread {
	private String threadName;
	BlockingQueue<Integer> queue;
	
	public Producer(String threadName, BlockingQueue<Integer> queue) {
		this.threadName = threadName;
		this.queue = queue;
	}
	
	public void run() {
		
		try {
			for (int i=0; i<10; i++) {
				queue.put(1);
				System.out.println("Produced: " + Integer.toString(i));
				Thread.sleep(300);
			}
		} catch (InterruptedException e) {
			System.out.println(e);
		}
	}
	
}

class Consumer extends Thread {
	private String threadName;
	BlockingQueue<Integer> queue;
	
	public Consumer(String threadName, BlockingQueue<Integer> queue) {
		this.threadName = threadName;
		this.queue = queue;
	}
	
	public void run() {
		try {
			for (int i=0; i<10; i++) {
				queue.remove();
				System.out.println("Consumed: " + Integer.toString(i));
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			System.out.println(e);
		}
	}
}