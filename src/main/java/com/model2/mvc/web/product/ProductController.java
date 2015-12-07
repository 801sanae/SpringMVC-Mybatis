package com.model2.mvc.web.product;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;



@Controller
public class ProductController {
	
	//Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	public ProductController() {
		System.out.println(this.getClass());
	}
	
	@Value("#{commonProperties['pageUnit']}")
	int pageUnit;
	@Value("#{commonProperties['pageSize']}")
	int pageSize;
	
	@RequestMapping("/addProduct.do")
	public String addUser( @ModelAttribute("prodcut") Product product, HttpServletRequest request ) throws Exception {
		
		if(FileUpload.isMultipartContent(request)){
			String temDir="C:\\workspace\\7.Model2MVCShop(final)\\WebContent\\images\\uploadFiles\\";
			
			DiskFileUpload fileUpload = new DiskFileUpload();
			fileUpload.setRepositoryPath(temDir);
			fileUpload.setSizeMax(1024*1024*10);
			fileUpload.setSizeThreshold(1024*100);
			
			if(request.getContentLength()<fileUpload.getSizeMax()){
				
				StringTokenizer token = null;
				
				List fileItemList=fileUpload.parseRequest(request);
				int Size = fileItemList.size();
				for (int i = 0; i < Size; i++) {
					
					FileItem fileItem = (FileItem)fileItemList.get(i);
					
					if(fileItem.isFormField()){
						if(fileItem.getFieldName().equals("manuDate")){
							token= new StringTokenizer(fileItem.getString("euc-kr"),"-");
							String manuDate = token.nextToken()+token.nextToken()+token.nextToken();
							product.setManuDate(manuDate);
						}
						else if(fileItem.getFieldName().equals("prodName"))
							product.setProdName(fileItem.getString("euc-kr"));
						else if(fileItem.getFieldName().equals("prodDetail"))
							product.setProdDetail(fileItem.getString("euc-kr"));
						else if(fileItem.getFieldName().equals("price"))
							product.setPrice(Integer.parseInt(fileItem.getString("euc-kr")));
					} else {
						
						if(fileItem.getSize()>0){
							int idx = fileItem.getName().lastIndexOf("\\");
							if(idx==-1){
								idx=fileItem.getName().lastIndexOf("/");
							}
							String fileName = fileItem.getName().substring(idx+1);
							product.setFileName(fileName);
							try{
								File uploadedFile= new File(temDir,fileName);
								fileItem.write(uploadedFile);
							} catch(IOException e){
								System.out.println(e);
							}
						}else{
							product.setFileName("../../images/empty.GIF");
						}
					}
				}
				productService.addProduct(product);
				
				request.setAttribute("prodvo", product);
			} else {
				int overSize = (request.getContentLength()/1000000);
				System.out.println("<script>alert('파일의 크기는 1MB까지 입니다. 올리신 파일용량은"+overSize+"MB입니다.");
				System.out.println("history.back();</script>");
			}
		} else {
			System.out.println("인코딩 타입이 mulitpart/form-data가 아닙니다.");
		}
		//System.out.println("/addProduct.do");
		//Business Logic
		//productService.addProduct(product);
		
		return "redirect:/listProduct.do?menu=search";
	}
	
	@RequestMapping("/getProduct.do")
	public String getProduct(@RequestParam("prodNo") String prodNo , Model model,
														HttpServletRequest request, HttpSession session) throws Exception {
		
		System.out.println("/getProduct.do");
		//Business Logic
		Product getProduct = productService.getProduct(Integer.parseInt(prodNo));
		
		boolean dual = true;

		List<Product> list = new ArrayList<Product>();

		if (session.getAttribute("producthistory") == null) {
			list.add(getProduct);
		} else if (session.getAttribute("producthistory") != null) {
			list = (List<Product>) session.getAttribute("producthistory");
			for (Product product : list) {
				if (getProduct.getProdNo() == product.getProdNo()) {
					dual = false;
				}
			}
		if (dual) {
				list.add(getProduct);
			}
		}

		session.setAttribute("producthistory", list);
		
		// Model 과 View 연결
		model.addAttribute("product", getProduct);
		
		if (request.getParameter("menu").equals("manage")) {
			return "forward:/product/updateProductView.jsp";
		} else {
			return "forward:/product/getProduct.jsp";
		}
	}
	
	@RequestMapping("/listProduct.do")
	public String listProduct(@ModelAttribute("serach") Search search, Model model , 
												HttpServletRequest request)throws Exception{
		System.out.println("/listProduct.do");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic 수행
		Map<String , Object> map=productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model 과 View 연결
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		
		if (request.getParameter("menu").equals("manage")) {
			return "forward:/product/listProduct.jsp";
		}else{
			return "forward:/product/readProduct.jsp";
		}
	}
	
	@RequestMapping("/updateProduct.do")
	public String updateProduct( @ModelAttribute("product") Product product, Model model, 
																		HttpSession session)throws Exception{
		System.out.println("/updateProduct.do");
		//Business Logic
		//int prodNo=Integer.parseInt(request.getParameter("prodNo"));
		//product.setProdNo(prodNo);
		
		productService.updateProduct(product);
		
		
		return "redirect:/getProduct.do?prodNo="+product.getProdNo()+"&menu=search";
		
	}
	
	@RequestMapping("/updateProductView.do")
	public String updateProductView(@RequestParam("prodNo") String prodNo , Model model ) throws Exception{
		
		System.out.println("/updateProductView.do");
		//Business Logic
		Product product = productService.getProduct(Integer.parseInt(prodNo));
		// Model 과 View 연결
		model.addAttribute("product", product);
		return "forward:/updateProduct.do";
	}
	
}
