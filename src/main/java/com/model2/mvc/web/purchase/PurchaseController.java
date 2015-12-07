package com.model2.mvc.web.purchase;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;


@Controller
public class PurchaseController {
	
	//Field
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	
	public PurchaseController() {
		System.out.println(this.getClass());
	}
	
	@Value("#{commonProperties['pageUnit']}")
	int pageUnit;
	@Value("#{commonProperties['pageSize']}")
	int pageSize;
	
	@RequestMapping("/addPurchaseView.do")
	public ModelAndView addPurchaseView(@ModelAttribute("purchase") Purchase purchase, HttpSession session,
												@RequestParam("prod_no") int prodNo)throws Exception{
		
		System.out.println("/addPurchaseView.do");
		
		purchase.setBuyer((User)session.getAttribute("user"));
		
		purchase.setPurchaseProd(productService.getProduct(prodNo));
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("forward:/purchase/addPurchaseView.jsp");
		modelAndView.addObject("purchase", purchase);
		
		return modelAndView;
	}
	
	@RequestMapping("/addPurchase.do")
	public ModelAndView addPurchase(@ModelAttribute("purchase") Purchase purchase, HttpSession session,
																			@RequestParam("prodNo") int prodNo)throws Exception{
		
		System.out.println("/addPurchase.do");
		//Business Logic
	
		
		purchase.setBuyer((User)session.getAttribute("user"));
		
		purchase.setPurchaseProd(productService.getProduct(prodNo));
		
		purchaseService.addPurchase(purchase);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("forward:/purchase/purchaseComplete.jsp");
		modelAndView.addObject("purchase", purchase);
		return modelAndView;
	}
	
	@RequestMapping("/getPurchase.do")
	public ModelAndView getPurchase(@ModelAttribute("purchase") Purchase purchase,
													@RequestParam("tranNo") int tranNo)throws Exception{
		System.out.println("/getPurchase.do");
		
		purchase=purchaseService.getPurchase(tranNo);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("forward:/purchase/getPurchase.jsp");
		modelAndView.addObject("purchase", purchase);
		return modelAndView;
	}
	
	@RequestMapping("/listPurchase.do")
	public String listPurchase(@ModelAttribute("serach") Search search, Model model , 
			HttpServletRequest request, HttpSession session)throws Exception{
		
		System.out.println("/listPurchase.do");
		
		User user = (User)session.getAttribute("user");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic 수행
		Map<String , Object> map=purchaseService.getPurchaseList(search,user.getUserId());
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model 과 View 연결
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		return "forward:/purchase/listPurchase.jsp";
	}
	
	@RequestMapping("/updatePurchaseView.do")
	public ModelAndView updatePurchaseView(@ModelAttribute("purchase") Purchase purchase,
			@RequestParam("tranNo") int tranNo)throws Exception{
		
		System.out.println("/updatePurchaseView.do");
		
		purchase = purchaseService.getPurchase(tranNo);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("forward:/purchase/UpdatePurchaseView.jsp");
		modelAndView.addObject("purchase", purchase);
		return modelAndView;
	}
	
	@RequestMapping("/updatePurchase.do")
	public ModelAndView updatePurchase(@ModelAttribute("purchase") Purchase purchase)throws Exception{
		
		System.out.println("/updatePurchase.do");
		
		purchaseService.updatePurchase(purchase);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("redirect:/getPurchase.do?tranNo="+purchase.getTranNo());
		modelAndView.addObject("purchase", purchase);
		return modelAndView;
	}
	
	@RequestMapping("/updateTranCode.do")
	public ModelAndView updateTranCode(@ModelAttribute("purchase") Purchase purchase,
									@RequestParam("prodNo") int prodNo, HttpServletRequest request )throws Exception{
		
		System.out.println("/updateTranCode.do");
		
		purchase.setPurchaseProd(productService.getProduct(prodNo));
		purchase.setTranCode((Integer.parseInt(request.getParameter("tran_status_code"))+1)+"");
		purchaseService.updateTranCode(purchase);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("forward:/listProduct.do");
		modelAndView.addObject("purchase", purchase);
		return modelAndView;
	}
	
	@RequestMapping("/updateTranCodeByProd")
	public ModelAndView updateTranCodeByProd(@ModelAttribute("purchase") Purchase purchase,
									@RequestParam("prodNo") int prodNo, 
									HttpServletRequest request )throws Exception{
	
		System.out.println("/updateTranCodeByProd.do");
		
		purchase.setPurchaseProd(productService.getProduct(prodNo));
		purchase.setTranCode((Integer.parseInt(request.getParameter("tran_status_code"))+1)+"");
		
		purchaseService.updateTranCode(purchase);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("forward:/listProduct.do");
		modelAndView.addObject("purchase", purchase);
		return modelAndView;
		
	}
	
}
