package com.lincz.blog.service;

import com.lincz.blog.entity.Tag;
import com.lincz.blog.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Override
    public List<Tag> getAllTag() {
        return tagRepository.findAll();
    }

    @Override
    public Tag createCategory(Tag tag) {
        return null;
    }

    @Override
    public Tag deleteTagByTagId(Long TagId) {
        return null;
    }

    @Override
    public Tag updateTag(Tag tag) {
        return null;
    }
}
