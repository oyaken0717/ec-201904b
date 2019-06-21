package com.example.ecommerce_b.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.ecommerce_b.domain.Order;
import com.example.ecommerce_b.service.OrderHistoryService;

/**
 * 注文をするためのコントローラー
 * 
 * @author ryuheisugita
 */
@Controller
@RequestMapping("/")
public class OrderHistoryController {
	
	@Autowired
	private OrderHistoryService orderHistoryService;
	
	@Autowired
	private HttpSession session;
	
	/**
	 * 注文履歴画面を表示する.
	 * 
	 * @param model
	 * @return　注文履歴画面
	 */
	@RequestMapping("/history")
	public String toOrderHistory(Model model) {
		Integer userId = (Integer) session.getAttribute("userId");
		List<Order> orderList = orderHistoryService.showOrderHistory(userId);
		if(orderList==null) {
			model.addAttribute("historyStatus",true);
			return "order_history";
		}
		model.addAttribute("orderList",orderList);
		System.out.println(orderList);
		System.out.println(orderList.get(0).getUser());
		System.out.println(orderList.get(0).getOrderItemList().get(0));
		System.out.println(orderList.get(0).getOrderItemList().get(0).getOrderToppingList().get(0));
		System.out.println(orderList.get(1));
		return "order_history";
	}
	
}