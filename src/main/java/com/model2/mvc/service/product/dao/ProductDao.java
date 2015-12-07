package com.model2.mvc.service.product.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;

@Repository("productDao")
public class ProductDao {
	
	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSession sqlSession;
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public void addProduct(Product product) throws Exception {
		sqlSession.insert("ProductMapper.addProduct", product);
	}

	public Product getProduct(int prodNo) throws Exception {
		
		return sqlSession.selectOne("ProductMapper.getProduct", prodNo);
	}

	public List<Product> getProductList(Search search) throws Exception {
		
		String keyword = search.getSearchKeyword();
		
		if(search.getSearchKeyword() != null && search.getSearchCondition().equals("1") && search.getSearchKeyword() !=""){
			search.setSearchKeyword("%"+keyword+"%");
		}
			
		List<Product> list = sqlSession.selectList("ProductMapper.getProductList", search);
		
		search.setSearchKeyword(keyword);
		
		for (int i = 0; i < list.size(); i++) {
			Purchase purchase = ((Purchase)sqlSession.selectOne("PurchaseMapper.getPurchase2" , list.get(i).getProdNo()));
			list.get(i).setProTranCode(purchase==null ? null : purchase.getTranCode().trim());
		}
		//Purchase purchase = sqlSession.selectOne("PurchaseMapper.getPurchase2", prodNo);
		return list;
	}
	
	public void updateProduct(Product product) throws Exception {
		sqlSession.update("ProductMapper.updateProduct", product);
	}
	
	public int removeProduct(String prodNo) throws Exception {
		return sqlSession.delete("ProductMapper.removeProduct", prodNo);
	}
	
	public int getTotalCount(Search search) throws Exception {
		return sqlSession.selectOne("ProductMapper.getTotalCount", search);
	}
}