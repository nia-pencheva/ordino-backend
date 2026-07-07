package com.ordino.support;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ordino.domain.products.model.entity.Product;
import com.ordino.domain.products.repository.ProductRepository;
import com.ordino.domain.recipes.categories.model.entity.RecipeCategory;
import com.ordino.domain.recipes.categories.repository.RecipeCategoryRepository;
import com.ordino.domain.recipes.logs.archive.model.entity.RecipeArchiveEvent;
import com.ordino.domain.recipes.logs.archive.repository.RecipeArchiveEventRepository;
import com.ordino.domain.recipes.logs.review.model.entity.RecipeReviewEvent;
import com.ordino.domain.recipes.logs.review.repository.RecipeReviewEventRepository;
import com.ordino.domain.recipes.model.entity.RecipeStatus;
import com.ordino.domain.recipes.repository.RecipeStatusRepository;
import com.ordino.domain.notifications.model.entity.NotificationType;
import com.ordino.domain.notifications.repository.NotificationTypeRepository;
import com.ordino.domain.units.model.entity.Unit;
import com.ordino.domain.units.model.entity.UnitCategory;
import com.ordino.domain.units.repository.UnitCategoryRepository;
import com.ordino.domain.units.repository.UnitRepository;
import com.ordino.domain.users.model.entity.Role;
import com.ordino.domain.users.model.entity.User;
import com.ordino.domain.users.repository.RoleRepository;
import com.ordino.domain.users.repository.UserRepository;
import com.ordino.domain.orders.model.entity.OrderStatus;
import com.ordino.domain.orders.repository.OrderStatusRepository;
import com.ordino.domain.suppliers.model.entity.Supplier;
import com.ordino.domain.suppliers.model.entity.SupplierProduct;
import com.ordino.domain.suppliers.repository.SupplierRepository;
import com.ordino.domain.suppliers.repository.SupplierProductRepository;
import com.ordino.domain.warehouse.products.model.entity.WarehouseProduct;
import com.ordino.domain.warehouse.products.repository.WarehouseProductRepository;
import com.ordino.domain.warehouse.warehouse_batches.logs.model.entity.WarehouseBatchEvent;
import com.ordino.domain.warehouse.warehouse_batches.logs.repository.WarehouseBatchEventRepository;
import com.ordino.domain.warehouse.warehouse_batches.model.entity.WarehouseBatch;
import com.ordino.domain.warehouse.warehouse_batches.repository.WarehouseBatchRepository;

import java.math.BigDecimal;

@Component
public class TestDataFactory {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RecipeStatusRepository recipeStatusRepository;

    @Autowired
    private RecipeReviewEventRepository recipeReviewEventRepository;

    @Autowired
    private RecipeArchiveEventRepository recipeArchiveEventRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private UnitCategoryRepository unitCategoryRepository;

    @Autowired
    private RecipeCategoryRepository recipeCategoryRepository;

    @Autowired
    private NotificationTypeRepository notificationTypeRepository;

    @Autowired
    private WarehouseProductRepository warehouseProductRepository;

    @Autowired
    private WarehouseBatchRepository warehouseBatchRepository;

    @Autowired
    private WarehouseBatchEventRepository warehouseBatchEventRepository;

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private SupplierProductRepository supplierProductRepository;

    private final AtomicLong uniqueSuffix = new AtomicLong();

    public Role role(String name) {
        return roleRepository.findByRoleIn(List.of(name)).stream()
                .findFirst()
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setRole(name);
                    return roleRepository.save(r);
                });
    }

    public User user(String username, String rawPassword, boolean passwordChangeRequired, String... roleNames) {
        List<Role> roles = Arrays.stream(roleNames).map(this::role).toList();

        long suffix = uniqueSuffix.incrementAndGet();

        User user = new User();
        user.setUsername(username);
        user.setFullName(username);
        user.setEmail(username + suffix + "@test.ordino.local");
        user.setPhoneNumber("+1000000" + suffix);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setPasswordChangedAt(passwordChangeRequired ? null : Instant.now());
        user.setRoles(roles);

        return userRepository.save(user);
    }

    public User lineCook(String username) {
        return user(username, "Passw0rd!", false, "line cook");
    }

    public User chef(String username) {
        return user(username, "Passw0rd!", false, "chef");
    }

    public User kitchenStaff(String username) {
        return user(username, "Passw0rd!", false, "kitchen staff");
    }

    public User warehouseManager(String username) {
        return user(username, "Passw0rd!", false, "warehouse manager");
    }

    public User manager(String username) {
        return user(username, "Passw0rd!", false, "manager");
    }

    public User admin(String username) {
        return user(username, "Passw0rd!", false, "admin");
    }

    public RecipeStatus recipeStatus(String name) {
        return recipeStatusRepository.findByStatus(name)
                .orElseGet(() -> {
                    RecipeStatus status = new RecipeStatus();
                    status.setStatus(name);
                    return recipeStatusRepository.save(status);
                });
    }

    public RecipeReviewEvent recipeReviewEvent(String name) {
        return recipeReviewEventRepository.findByEvent(name)
                .orElseGet(() -> {
                    RecipeReviewEvent event = new RecipeReviewEvent();
                    event.setEvent(name);
                    return recipeReviewEventRepository.save(event);
                });
    }

    public RecipeArchiveEvent recipeArchiveEvent(String name) {
        return recipeArchiveEventRepository.findByEvent(name)
                .orElseGet(() -> {
                    RecipeArchiveEvent event = new RecipeArchiveEvent();
                    event.setEvent(name);
                    return recipeArchiveEventRepository.save(event);
                });
    }

    /** Seeds all recipe status/review-event/archive-event lookup rows (normally Flyway-seeded). */
    public void seedRecipeReferenceData() {
        List.of("DRAFT", "WAITING_FOR_APPROVAL", "RETURNED_FOR_REVISION", "DISCARDED", "APPROVED", "ARCHIVED")
                .forEach(this::recipeStatus);
        List.of("SUBMITTED_FOR_APPROVAL", "RETURNED_FOR_REVISION", "DISCARDED", "APPROVED")
                .forEach(this::recipeReviewEvent);
        List.of("ARCHIVED", "RETURNED_TO_APPROVED")
                .forEach(this::recipeArchiveEvent);
        seedNotificationTypes();
    }

    public NotificationType notificationType(String name) {
        return notificationTypeRepository.findByType(name)
                .orElseGet(() -> {
                    NotificationType type = new NotificationType();
                    type.setType(name);
                    return notificationTypeRepository.save(type);
                });
    }

    /** Seeds all notification-type lookup rows referenced by WarehouseNotificationServiceImpl. */
    public void seedNotificationTypes() {
        List.of("NEW_PRODUCT", "LOW_QUANTITY", "BATCH_EXPIRING").forEach(this::notificationType);
    }

    public Product product(String name) {
        return productRepository.findAll().stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseGet(() -> {
                    Product product = new Product();
                    product.setName(name);
                    product.setActive(true);
                    return productRepository.save(product);
                });
    }

    public UnitCategory unitCategory(String name) {
        return unitCategoryRepository.findAll().stream()
                .filter(uc -> uc.getCategory().equals(name))
                .findFirst()
                .orElseGet(() -> {
                    UnitCategory category = new UnitCategory();
                    category.setCategory(name);
                    category.setUnits(new java.util.ArrayList<>());
                    return unitCategoryRepository.save(category);
                });
    }

    public Unit unit(String name, String abbreviation) {
        return unitRepository.findAll().stream()
                .filter(u -> u.getAbbreviation().equals(abbreviation))
                .findFirst()
                .orElseGet(() -> {
                    Unit unit = new Unit();
                    unit.setUnit(name);
                    unit.setAbbreviation(abbreviation);
                    unit.setUnitCategory(unitCategory("Weight"));
                    return unitRepository.save(unit);
                });
    }

    public RecipeCategory recipeCategory(String name) {
        return recipeCategoryRepository.findAll().stream()
                .filter(rc -> rc.getCategory().equals(name))
                .findFirst()
                .orElseGet(() -> {
                    RecipeCategory category = new RecipeCategory();
                    category.setCategory(name);
                    return recipeCategoryRepository.save(category);
                });
    }

    public WarehouseProduct warehouseProduct(Product product, Unit unit, BigDecimal minQuantity) {
        WarehouseProduct warehouseProduct = new WarehouseProduct();
        warehouseProduct.setProduct(product);
        warehouseProduct.setUnit(unit);
        warehouseProduct.setMinQuantity(minQuantity);
        warehouseProduct.setActive(true);
        warehouseProduct.setWarehouseBatches(new java.util.ArrayList<>());
        warehouseProduct.setSupplierProducts(new java.util.ArrayList<>());
        warehouseProduct.setOrderProducts(new java.util.ArrayList<>());
        return warehouseProductRepository.save(warehouseProduct);
    }

    public WarehouseBatch warehouseBatch(WarehouseProduct warehouseProduct, BigDecimal quantity) {
        WarehouseBatch batch = new WarehouseBatch();
        batch.setWarehouseProduct(warehouseProduct);
        batch.setQuantity(quantity);
        batch.setEventLogs(new java.util.ArrayList<>());
        WarehouseBatch saved = warehouseBatchRepository.save(batch);
        warehouseProduct.getWarehouseBatches().add(saved);
        return saved;
    }

    public WarehouseBatchEvent warehouseBatchEvent(String type) {
        return warehouseBatchEventRepository.findByType(type)
                .orElseGet(() -> {
                    WarehouseBatchEvent event = new WarehouseBatchEvent();
                    event.setType(type);
                    return warehouseBatchEventRepository.save(event);
                });
    }

    /** Seeds all warehouse batch event lookup rows (normally Flyway-seeded). */
    public void seedWarehouseBatchEvents() {
        List.of("USED", "LOST", "RECEIVED").forEach(this::warehouseBatchEvent);
    }

    public OrderStatus orderStatus(String name) {
        return orderStatusRepository.findByStatus(name)
                .orElseGet(() -> {
                    OrderStatus status = new OrderStatus();
                    status.setStatus(name);
                    return orderStatusRepository.save(status);
                });
    }

    /** Seeds all order status lookup rows (normally Flyway-seeded). */
    public void seedOrderStatuses() {
        List.of("PENDING", "RECEIVED", "CANCELLED").forEach(this::orderStatus);
    }

    public Supplier supplier(String name) {
        long suffix = uniqueSuffix.incrementAndGet();

        Supplier supplier = new Supplier();
        supplier.setName(name);
        supplier.setAddress("123 Test Street");
        supplier.setEmail(name.toLowerCase().replace(" ", ".") + suffix + "@supplier.test.local");
        supplier.setPhoneNumber("+3591234" + String.format("%05d", suffix % 100000));
        supplier.setActive(true);
        supplier.setSupplierProducts(new java.util.ArrayList<>());
        supplier.setOrders(new java.util.ArrayList<>());
        return supplierRepository.save(supplier);
    }

    public SupplierProduct supplierProduct(Supplier supplier, WarehouseProduct warehouseProduct, BigDecimal price, BigDecimal minOrderQuantity) {
        SupplierProduct supplierProduct = new SupplierProduct();
        supplierProduct.setSupplier(supplier);
        supplierProduct.setWarehouseProduct(warehouseProduct);
        supplierProduct.setPrice(price);
        supplierProduct.setMinOrderQuantity(minOrderQuantity);
        SupplierProduct saved = supplierProductRepository.save(supplierProduct);
        supplier.getSupplierProducts().add(saved);
        warehouseProduct.getSupplierProducts().add(saved);
        return saved;
    }
}
