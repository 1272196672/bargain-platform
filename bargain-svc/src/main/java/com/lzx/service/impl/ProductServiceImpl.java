package com.lzx.service.impl;

import com.lzx.entity.Product;
import com.lzx.mapper.ProductMapper;
import com.lzx.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Bobby.zx.lin
 * @since 2023-02-24
 */
@Service
@Slf4j
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

}
