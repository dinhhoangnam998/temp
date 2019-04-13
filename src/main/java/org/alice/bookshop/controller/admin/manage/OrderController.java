package org.alice.bookshop.controller.admin.manage;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.alice.bookshop.model.Order;
import org.alice.bookshop.service.admin.manage.OrderService;
import org.alice.bookshop.service.common.ulti.PaginationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("amOrderController")
@RequestMapping("/admin/manage/orders")
public class OrderController {

	final int CANCELED = -1;
	final int ORDERING = 0;
	final int ORDERED = 1;
	final int DELIVERING = 2;
	final int SUCCESSED = 3;

	@Autowired
	private OrderService orderService;

	@Autowired
	private PaginationService pagi;

	@GetMapping
	public String show(HttpSession ss, Model model, @RequestParam(required = false, defaultValue = "1") int p,
			@RequestParam(required = false, defaultValue = "15") int psize) {

		pagi.process(ss, p, psize, orderService.orderJpa.count());

		// get order page
		List<Order> orders = orderService.getOrders(p, pagi.getPageSize());
		model.addAttribute("orders", orders);

		// pagination
		List<Integer> pageList = pagi.getPageList();
		model.addAttribute("pages", pageList);

		return "/admin/manage/orders/show";
	}

	@GetMapping("/{id}/change-state")
	public String changeOrderState(RedirectAttributes redirAtrr, @RequestParam int s, @PathVariable int id) {
		Order order = orderService.orderJpa.getOne(id);
		order.setState(s);
//
//		switch (state) {
//		case CANCELED:
//			order4change.setState(CANCELED);
//			msg = "CANCELED the Order";
//			break;
//
//		case DELIVERING:
//			order4change.setState(DELIVERING);
//			msg = "CANCELED the Order";
//			break;
//
//		case SUCCESSED:
//			order4change.setState(SUCCESSED);
//			break;
//
//		default:
//			msg = "Nothing happend";
//			break;
//		}
//		
		orderService.orderJpa.save(order);

//		redirAtrr.addFlashAttribute("msg", msg);
		return "redirect:/admin/manage/orders?p=" + pagi.getCurPage();
	}

}