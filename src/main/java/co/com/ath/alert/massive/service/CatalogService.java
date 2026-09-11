package co.com.ath.alert.massive.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.ath.alert.massive.model.entity.Catalog;
import co.com.ath.alert.massive.repository.ICatalogRepository;

@Service
public class CatalogService {

	private static final Logger log = LoggerFactory.getLogger(CatalogService.class);

	@Autowired
	ICatalogRepository iCatalogRepository;

	public Catalog findCatalog(String nameCatalogType, String key, String bankId) {
		Catalog catalog = null;
		try {
			catalog = iCatalogRepository.findByNameWithQuery(nameCatalogType, key, bankId);
		} catch (Exception e) {
			log.error("Error "+e);
		}
		return catalog;
	}
}
