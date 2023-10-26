package com.iternova.ecommerce.service;

import com.iternova.ecommerce.exception.ProductException;
import com.iternova.ecommerce.model.Category;
import com.iternova.ecommerce.model.Product;
import com.iternova.ecommerce.model.Size;
import com.iternova.ecommerce.repository.CategoryRepository;
import com.iternova.ecommerce.repository.ProductRepository;
import com.iternova.ecommerce.repository.SizeRepository;
import com.iternova.ecommerce.request.CreateProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.List.of;

@Service
public class ProductServiceImp implements ProductService{

    private ProductRepository productRepository;
    private SizeRepository sizeRepository;

    private UserService userService;
    private CategoryRepository categoryRepository;

    public ProductServiceImp(SizeRepository sizeRepository, ProductRepository productRepository, UserService userService, CategoryRepository categoryRepository){
        this.productRepository = productRepository;
        this.userService=userService;
        this.categoryRepository = categoryRepository;
        this.sizeRepository = sizeRepository;
    }


    @Override
    public Product createProduct(CreateProductRequest req) {

        Category topLevel = categoryRepository.findByName(req.getTopLabelCategory());

        if(topLevel == null){
            Category topLevelCategory = new Category();
            topLevelCategory.setName(req.getTopLabelCategory());
            topLevelCategory.setLevel(1);

            topLevel = categoryRepository.save(topLevelCategory);
        }

        Category secondLevel = categoryRepository.findByNameAndParent(req.getSecondLabelCategory(),topLevel.getName());

        if(secondLevel == null){
            Category secondLevelCategory = new Category();
            secondLevelCategory.setName(req.getSecondLabelCategory());
            secondLevelCategory.setParentCategory(topLevel);
            secondLevelCategory.setLevel(2);

            secondLevel = categoryRepository.save(secondLevelCategory);
        }

        Category thirdLevel = categoryRepository.findByNameAndParent(req.getThirdLabelCategory(),secondLevel.getName());

        if(thirdLevel == null){
            Category thirdLevelCategory = new Category();
            thirdLevelCategory.setName(req.getThirdLabelCategory());
            thirdLevelCategory.setParentCategory(secondLevel);
            thirdLevelCategory.setLevel(3);

            thirdLevel = categoryRepository.save(thirdLevelCategory);
        }

    Product product = new Product();
        product.setTitle(req.getTitle());
        product.setColor(req.getColor());
        product.setDescription(req.getDescription());
        product.setDiscountedPrice(req.getDiscountedPrice());
        product.setDiscountPercent(req.getDiscountPercent());
        product.setImageUrl(req.getImageUrl());
        product.setBrand(req.getBrand());
        product.setPrice(req.getPrice());
        //product.setSizes(req.getSize());
        product.setQuantity(req.getQuantity());
        product.setCategory(thirdLevel);
        product.setCreatedAt(LocalDateTime.now());

        Set<Size> sizes = new HashSet<>();
        for (Size sizeRequest : req.getSize()) {
            Size size = new Size();
            size.setName(sizeRequest.getName());
            size.setQuantity(sizeRequest.getQuantity());
            // Guardar el objeto Size
            size = sizeRepository.save(size);
            sizes.add(size);
        }
        product.setSizes(sizes);

        Product savedProduct = productRepository.save(product);
        System.out.println("Products" + product);

        return savedProduct;
    }

    @Override
    public String deleteProduct(Long productId) throws ProductException {
       Product product = findProductById(productId);
       product.getSizes().clear();
       productRepository.delete(product);
       return "product deleted successfully";
    }

    @Override
    public Product updateProduct(Long productId, Product req) throws ProductException {

        Product product =findProductById(productId);
        if(req.getQuantity()!=0){
            product.setQuantity(req.getQuantity());
        }

        return productRepository.save(product);
    }

    @Override
    public Product findProductById(Long id) throws ProductException {
        Optional<Product> opt = productRepository.findById(id);

        if(opt.isPresent()) {
            return  opt.get();
        }
        throw  new ProductException("Product not found with id " + id);
        }

    @Override
    public List<Product> findProductByategory(String category) {
        return null;
    }

    @Override
    public Page<Product> getAllProducts(String category, List<String> colors, List<String> sizes,
                                        Integer minPrice, Integer maxPrice,
                                        Integer minDiscount, String sort,
                                        String stock, Integer pageNumber, Integer pageSize) {
        //creamos un objeto Pageable, con el número de página solicitado y el tamaño de la página
        Pageable pageable = PageRequest.of(pageNumber,pageSize);

        //traemos una lista incial de productos según los parámetros pasados
        List<Product> products = productRepository.filterProducts(category,minPrice,maxPrice,minDiscount,sort);

        //iniciamos un primer filtro según colores, utilizamos stream para filtrar y devolver en la misma lista
        if(!colors.isEmpty()){
            products = products.stream().filter(p-> colors.stream().anyMatch(c-> c.equalsIgnoreCase(p.getColor())))
                    .collect(Collectors.toList());
        }

        //ahora se filtran depende si hay stock o no
        if(stock!= null){
            if(stock.equals("in_stock")){
                products = products.stream().filter(p -> p.getQuantity()>0).collect(Collectors.toList());
            } else if (stock.equals("out_of_stock")) {
                products = products.stream().filter(p-> p.getQuantity()<1).collect(Collectors.toList());
            }
        }
        //Se calcula el índice de inicio y el índice de fin para obtener los elementos necesarios
        // de la lista de productos para la página actual.
        // Esto se hace en función de la información de paginación proporcionada por pageable.
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex+pageable.getPageSize(), products.size());

        System.out.println("pagina de inicio: "+ startIndex);
        System.out.println("pagina final: " + endIndex);

        //Se obtiene el contenido de la página actual extrayendo los elementos de la lista products
        // entre startIndex y endIndex.
        List<Product> pageContent = products.subList(startIndex,endIndex);


        //se crea un objeto Page con el contenido de la pagina, la info de paginación y el tamaño total de la lista
        Page<Product> filteredProducts = new PageImpl<>(pageContent,pageable, products.size());

        return filteredProducts;
    }

    @Override
    public List<Product> findAllproducts() {
        List<Product> products = productRepository.findAll();

        return products;
    }

    @Override
    public List<Product> findProductsByCategory(String category) {
        List<Product> products = productRepository.filterProductsByCategory(category);
        return products;
    }
}
