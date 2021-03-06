package com.example.ecommerce_b.controller;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.ecommerce_b.domain.Order;
import com.example.ecommerce_b.form.OrderForm;
import com.example.ecommerce_b.service.OrderService;
import com.example.ecommerce_b.service.SendMailService;

/**
 * 注文をするためのコントローラー
 * 
 * @author ryuheisugita
 */
@Controller
@RequestMapping("/")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private SendMailService sendMailService;
	
	
	/**
	 * 使用するフォームオブジェクトをリクエストスコープに格納する.
	 * 
	 * @return フォーム
	 */
	@ModelAttribute
	public OrderForm setUpOrderForm() {
		return new OrderForm();
	}
	
	/**
	 * 注文確認画面を表示する.
	 * 
	 * @param model
	 * @return　注文確認画面
	 */
	@RequestMapping("/to-order")
	public String toOrder(Model model) {
		Integer userId = (Integer) session.getAttribute("userId");
		Order order = orderService.serchByUserIdNotOrdered(userId);
		model.addAttribute("order",order);
		model.addAttribute("tax",order.getTax());
		model.addAttribute("totalPrice",order.getCalcTotalPrice());
		return "order_confirm";
	}
	
	/**
	 * 注文をする.
	 * 
	 * @param form
	 * @return 注文確認画面
	 */
	@RequestMapping("/order")
	public String order(@Validated OrderForm form,
			                   BindingResult result,
			                    Model model ){
		if(result.hasErrors()) {
			return toOrder(model);
		}
		
		String strDeliveryTime = form.getDeliveryDate() + " " + form.getDeliveryHour() +":00:00";
		
		Integer userId = (Integer) session.getAttribute("userId");
		
		Order order = orderService.serchByUserIdNotOrdered(userId);
		BeanUtils.copyProperties(form, order);
		
		order.setDeliveryTime(Timestamp.valueOf(strDeliveryTime));
		
		order.setOrderDate(new Date());
		
		if(form.getPaymentMethod() == 1) {
			order.setStatus(1);
		}else {
			order.setStatus(2);			
		}
		
		orderService.order(order);
		sendMailService.sendMail(order);
		
		return "redirect:/finish";
	}
	
	/**
	 * 決済画面を表示する.
	 * 
	 * @return 決済画面
	 */
	@RequestMapping("/finish")
	public String finish() {
		return "order_finished";
	}

}
