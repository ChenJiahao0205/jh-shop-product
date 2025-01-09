package pers.jhshop.product.consts;

/**
 * Redis key常量类
 * @author ChenJiahao(五条)
 * @date 2025/01/09 22:29:36
 */
public interface RedisKeyConstants {

    String PRODUCT_REDIS_KEY_PREFIX = "product-";

    /**
     * 获取所有商品分类Redis
     */
    String ALL_PRODUCT_CATEGORY_REDIS_KEY = PRODUCT_REDIS_KEY_PREFIX + "allProductCategoryRedisKey";

    /**
     * 获取所有商品标签Redis
     */
    String ALL_PRODUCT_TAG_REDIS_KEY = PRODUCT_REDIS_KEY_PREFIX + "allProductTagRedisKey";

}
