package com.greglturnquist.hackingspringboot.reactive;

//@SpringBootApplication
//public class HackingSpringBootApplicationBlockHoundCustomized {
//    // tag::blockhound[]
//    public static void main(String[] args) {
//        BlockHound.builder() // <1>
//                .allowBlockingCallsInside( //
//                        TemplateEngine.class.getCanonicalName(), "process") // <2>
//                .install(); // <3>
//
//        SpringApplication.run(HackingSpringBootApplicationBlockHoundCustomized.class, args);
//    }
//    // end::blockhound[]
//}
