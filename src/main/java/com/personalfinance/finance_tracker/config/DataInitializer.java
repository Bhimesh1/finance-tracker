package com.personalfinance.finance_tracker.config;

import com.personalfinance.finance_tracker.model.Category;
import com.personalfinance.finance_tracker.model.User;
import com.personalfinance.finance_tracker.repository.CategoryRepository;
import com.personalfinance.finance_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    private boolean alreadySetup = false;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        createDefaultCategoriesForExistingUsers();

        alreadySetup = true;
    }

    private void createDefaultCategoriesForExistingUsers() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            createIncomeCategories(user);
            createExpenseCategories(user);
        }
    }

    public void createDefaultCategoriesForUser(User user) {
        createIncomeCategories(user);
        createExpenseCategories(user);
    }

    private void createIncomeCategories(User user) {
        List<String> incomeCategories = Arrays.asList(
                "Salary", "Freelance", "Investments", "Gifts", "Other Income");

        for (String categoryName : incomeCategories) {
            createCategoryIfNotExists(categoryName, "Income category: " + categoryName,
                    Category.CategoryType.INCOME, "#4CAF50", user);
        }
    }

    private void createExpenseCategories(User user) {
        List<String> expenseCategories = Arrays.asList(
                "Housing", "Food", "Transportation", "Entertainment", "Shopping",
                "Utilities", "Healthcare", "Education", "Travel", "Miscellaneous");

        for (String categoryName : expenseCategories) {
            createCategoryIfNotExists(categoryName, "Expense category: " + categoryName,
                    Category.CategoryType.EXPENSE, "#F44336", user);
        }
    }

    @Transactional
    protected void createCategoryIfNotExists(String name, String description,
                                             Category.CategoryType type, String color, User user) {
        if (categoryRepository.findByUserIdAndType(user.getId(), type).stream()
                .noneMatch(category -> category.getName().equals(name))) {
            Category category = new Category();
            category.setName(name);
            category.setDescription(description);
            category.setType(type);
            category.setColor(color);
            category.setUser(user);

            categoryRepository.save(category);
        }
    }
}