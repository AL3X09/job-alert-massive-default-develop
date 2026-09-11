package co.com.ath.alert.massive.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.ath.alert.massive.model.entity.Product;
import co.com.ath.alert.massive.repository.IProductRepository;

@Service
public class ProductService {

	private static final Logger log = LoggerFactory.getLogger(ProductService.class);

	@Autowired
	IProductRepository iProductRepository;

	public void registerProduct(String acctId, String bankId, String acctType, String nickName, int clientId) {
		Product newProduct = null;
		try {
			newProduct = iProductRepository.findByProductId(acctId, bankId);
			
			if (newProduct == null) {
				Product productObj = new Product();
				productObj.setProductId(acctId);
				productObj.setBankId(bankId);
				productObj.setBankProductType(acctType);
				productObj.setNickName(nickName);
				productObj.setClientId(clientId);
				iProductRepository.save(productObj);
				log.info("Producto registrado con exito!");
			} else {
				log.info("Ya existe producto en BD alertas");
			}
		}catch(Exception e) {
			log.info("Error al intentar crear un producto "+ e);
		}
	}
}
