package com.apn.ecomercAP.service.Impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.apn.ecomercAP.model.Product;
import com.apn.ecomercAP.repository.ProductRepository;
import com.apn.ecomercAP.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Override
	public Product saveProduct(Product product) {
		// TODO Auto-generated method stub
		return productRepository.save(product);
	}

	@Override
	public List<Product> getAllProducts() {
		// TODO Auto-generated method stub
		return productRepository.findAll();
	}

	@Override
	public List<Product> searchProduct(String ch1, String ch2) {
		// TODO Auto-generated method stub
		return productRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch1, ch2);
	}

	@Override
	public Page<Product> getAllActiveProductPagination(Integer pageNo, Integer pageSize, String category) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);

		Page<Product> pageProduct = null;

		if (ObjectUtils.isEmpty(category)) {
			pageProduct = productRepository.findByIsActiveTrue(pageable);
		} else {
			pageProduct = productRepository.findByCategory(pageable, category);
		}
		return pageProduct;
	}

	@Override
	public Page<Product> getAllProductsPagination(Integer pageNo, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);

		Page<Product> pageProduct = productRepository.findAll(pageable);
		return pageProduct;
	}

	@Override
	public Page<Product> searchProductPagination(Integer pageNo, Integer pageSize, String ch) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		return productRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch, ch, pageable);
	}

	@Override
	public Page<Product> searchActiveProductPagination(Integer pageNo, Integer pageSize, String ch) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);

		Page<Product> pageProduct = productRepository
				.findByisActiveTrueAndTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch, ch, pageable);
		return pageProduct;
	}

	@Override
	public Product getProductById(Integer id) {

		return productRepository.findById(id).orElse(null);
	}

	@Override
	public List<Product> getAllActiveProducts(String category) {
		// TODO Auto-generated method stub
		return productRepository.findByIsActiveTrue();
	}

	@Override
	public Page<Product> searchDiscountPricePagination(Integer pageNo, Integer pageSize, String sort) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);

		if ("price_asc".equals(sort)) {
			return productRepository.findByIsActiveTrueOrderByDiscountPriceAsc(pageable);
		} else if ("price_desc".equals(sort)) {
			return productRepository.findByIsActiveTrueOrderByDiscountPriceDesc(pageable);
		} else {
			// Mặc định, không sắp xếp theo giá
			return productRepository.findByIsActiveTrue(pageable);
		}

	}

	@Override
	public Product updateProduct(Product product, MultipartFile image) {
		Product dbProduct = getProductById(product.getId());

		String imageName = image.isEmpty() ? dbProduct.getImage() : image.getOriginalFilename();

		dbProduct.setTitle(product.getTitle());
		dbProduct.setDescription(product.getDescription());
		dbProduct.setCategory(product.getCategory());
		dbProduct.setPrice(product.getPrice());
		dbProduct.setStock(product.getStock());
		dbProduct.setImage(imageName);
		dbProduct.setIsActive(product.getIsActive());
		dbProduct.setDiscount(product.getDiscount());

		// price :100, discount 5%-->
		Double discountPrice = product.getPrice() * (100 - product.getDiscount()) / 100;

		dbProduct.setDiscountPrice(discountPrice);
		// save
		Product updateProduct = productRepository.save(dbProduct);
		if (!ObjectUtils.isEmpty(updateProduct)) {

			if (!image.isEmpty()) {

				try {
					File saveFile = new ClassPathResource("static/img").getFile();

					Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator
							+ image.getOriginalFilename());
					Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return product;
		}
		return null;
	}

	@Override
	public Boolean deleteProduct(Integer id) {
		Product product = productRepository.findById(id).orElse(null);
		if (!ObjectUtils.isEmpty(product)) {
			productRepository.delete(product);
			return true;
		}
		return false;
	}

	@Override
	public Page<Product> findTop4ActiveProducts(Integer pageNo, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		// TODO Auto-generated method stub
		return productRepository.findTop4ActiveProducts(pageable);
	}

}
