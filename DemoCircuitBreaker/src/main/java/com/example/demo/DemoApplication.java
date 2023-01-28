package com.example.demo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication  {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

        DummyService service = new DummyService(true);
        Foo foo = new Foo(service, 5);

        service.registerService(foo);

        for (int i = 1; i <= 12; i++) {
            if (i == 5) {
                service.setState(false);
            }

            if (i == 9) {
                service.setState(true);
            }

            String response = foo.sendRequest("request " + i);
            if (response != null) {
                System.out.println(response);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
		}
	}

}
