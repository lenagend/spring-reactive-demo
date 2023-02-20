/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kkm.springReactiveDemo;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.result.view.Rendering;

/**
 * @author Greg Turnquist
 */
// tag::1[]
@Controller // <1>
public class HomeController {

	private InventoryService inventoryService;

	public HomeController(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}
	// end::1[]

	// tag::2[]      
	@GetMapping
	Mono<Rendering> home() { // <1>
		return Mono.just(Rendering.view("home.html") // <2>
				.modelAttribute("items", //
						this.inventoryService.getInventory()
								.doOnNext(System.out::println)) // <3>
				.modelAttribute("cart", //
						this.inventoryService.getCart("My Cart") // <4>
								.defaultIfEmpty(new Cart("My Cart")))
				.build());
	}
	// end::2[]

	// tag::3[]
	@PostMapping("/add/{id}") // <1>
	Mono<String> addToCart(@PathVariable String id) { // <2>
		return this.inventoryService.addItemToCart("My Cart", id).thenReturn("redirect:/");
	}
	// end::3[]


	@PostMapping
	Mono<String> createItem(@ModelAttribute Item newItem) {
		return this.inventoryService.saveItem(newItem) //
				.thenReturn("redirect:/");
	}

	@DeleteMapping("/delete/{id}")
	Mono<String> deleteItem(@PathVariable String id) {
		return this.inventoryService.deleteItem(id) //
				.thenReturn("redirect:/");
	}


}
