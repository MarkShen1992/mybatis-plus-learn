package io.markshen.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 分层思想
 * @param <T>
 */
public interface CommonDAO<T> extends BaseMapper<T> {

    /**
     * 删除所有数据
     * @return
     */
    int deleteAll();
}
