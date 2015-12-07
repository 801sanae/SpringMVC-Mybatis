package com.model2.mvc.service.purchase.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;

@Repository("purchaseDAO")
public class PurchaseDAO {
	
	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSession sqlSession;
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public void addPurchase(Purchase purchase) throws Exception {
		
		sqlSession.insert("PurchaseMapper.addPurchase", purchase);
	}
	
	public List<Purchase> getPurchaseList(Search search, String buyerId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("search", search);
		map.put("buyerId", buyerId);
		
		List<Purchase> list = sqlSession.selectList("PurchaseMapper.getPurchaseList", map);
		
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setBuyer((User)sqlSession.selectOne("UserMapper.getUser", buyerId));
			list.get(i).setPurchaseProd((Product)sqlSession.selectOne("ProductMapper.getProduct" , list.get(i).getPurchaseProd().getProdNo()));
		}
		
		return list;
	}
	
	public Purchase getPurchase(int tranNo) throws Exception {
	
		return sqlSession.selectOne("PurchaseMapper.getPurchase", tranNo);
	}
	public Purchase getPurchase2(int prodNo) throws Exception {
		
		return sqlSession.selectOne("PurchaseMapper.getPurchase", prodNo);
	}
	
	public void updatePurchase(Purchase purchase) throws Exception {
			
		sqlSession.update("PurchaseMapper.updatePurchase", purchase);	
	}
	public void updateTranCode(Purchase purchase)throws Exception {
		
		sqlSession.update("PurchaseMapper.updateTranCode", purchase);	
	}
	public int getTotalCount( String buyerId) throws Exception {
		return sqlSession.selectOne("PurchaseMapper.getTotalCount", buyerId);
	}
}
	