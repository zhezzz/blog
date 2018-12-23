package com.lincz.blog.service;

import com.lincz.blog.entity.Tag;

import java.util.List;

public interface TagService {

    List<Tag> getAllTag();

    Tag createCategory(Tag tag);

    void deleteTagByTagId(Long tagId);

    Tag updateTag(Long tagId,Tag formTag);

}
