package com.dev.getup.services.impl;

import com.dev.getup.domain.entities.Category;
import com.dev.getup.repositories.CategoryRepo;
import com.dev.getup.services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;
    @Override
    public List<Category> listCategories() {
        return categoryRepo.findAllWithPostCount();
    }

    @Override
    @Transactional
    public Category createCategory(Category category) {
        if (categoryRepo.existsByNameIgnoreCase(category.getName())) {
            throw new IllegalArgumentException("Category with name " + category.getName() + " already exists");
        }
        return categoryRepo.save(category);
    }

    @Override
    public void deleteCategory(UUID id) {
        Optional<Category> category = categoryRepo.findById(id);
        if (category.isPresent()) {
            if (!category.get().getPosts().isEmpty()) {
                throw new IllegalStateException("Category has posts associated");
            }
        }
        categoryRepo.deleteById(id);

    }

    @Override
    public Category getCategoryById(UUID id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + id + " does not exist"));
    }
}
