package com.lincz.blog.service;

import com.lincz.blog.entity.Tag;
import com.lincz.blog.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

	@Autowired
	private TagRepository tagRepository;

	@Override
	public Tag getTagByTagId(Long tagId) {
		return tagRepository.findById(tagId).orElse(null);
	}

	@Override
	public List<Tag> getAllTag() {
		return tagRepository.findAll();
	}

	@Override
	public Tag createCategory(Tag tagDTO) {
		Tag tag = new Tag(tagDTO.getTagName());
		return tagRepository.save(tag);
	}

	@Override
	public void deleteTagByTagId(Long tagId) {
		tagRepository.deleteById(tagId);
	}

	@Override
	public Tag updateTag(Long tagId, Tag tagDTO) {
		Tag tag = tagRepository.findById(tagId).orElse(null);
		tag.setTagName(tagDTO.getTagName());
		return tagRepository.save(tag);
	}

	@Override
	public boolean isTagExists(Long tagId) {
		return tagRepository.existsById(tagId);
	}
}
