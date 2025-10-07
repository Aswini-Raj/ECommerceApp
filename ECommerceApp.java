package ecommerce;

import java.util.*;

// Product Class
class Product {
    private int productId;
    private String name;
    private double price;
    private int stockQuantity;

    public Product(int productId, String name, double price, int stockQuantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public int getProductId() { return productId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStockQuantity() { return stockQuantity; }

    public void decreaseStock(int qty) { if (qty <= stockQuantity) stockQuantity -= qty; }
    public void increaseStock(int qty) { stockQuantity += qty; }

    @Override
    public String toString() {
        return "Product ID: " + productId + " | " + name + " | Price: ₹" + price + " | Stock: " + stockQuantity;
    }
}

// Customer Class
class Customer {
    private int customerId;
    private String name;
    private String email;

    public Customer(int customerId, String name, String email) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
    }

    public int getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return "Customer ID: " + customerId + " | Name: " + name + " | Email: " + email;
    }
}

// OrderItem Class
class OrderItem {
    private Product product;
    private int quantity;

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public double getItemTotal() { return product.getPrice() * quantity; }
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }

    @Override
    public String toString() { return product.getName() + " x " + quantity + " = ₹" + getItemTotal(); }
}

// Order Class
class Order {
    private int orderId;
    private Customer customer;
    private List<OrderItem> items = new ArrayList<>();
    private boolean paid;
    private boolean shipped;

    public Order(int orderId, Customer customer) { this.orderId = orderId; this.customer = customer; }

    public void addItem(OrderItem item) { items.add(item); }
    public double getTotal() { return items.stream().mapToDouble(OrderItem::getItemTotal).sum(); }
    public void setPaid(boolean paid) { this.paid = paid; }
    public void setShipped(boolean shipped) { this.shipped = shipped; }
    public boolean isPaid() { return paid; }
    public boolean isShipped() { return shipped; }
    public Customer getCustomer() { return customer; }
    public List<OrderItem> getItems() { return items; }

    @Override
    public String toString() {
        return "Order ID: " + orderId + " | Customer: " + customer.getName() + " | Total: ₹" + getTotal() + " | Paid: " + paid + " | Shipped: " + shipped;
    }
}

// Payment Class
class Payment {
    private int paymentId;
    private double amount;
    private String status;

    public Payment(int paymentId, double amount) { this.paymentId = paymentId; this.amount = amount; this.status = "Pending"; }
    public boolean processPayment() { this.status = "Successful"; return true; }
    public String getStatus() { return status; }
    @Override
    public String toString() { return "Payment ID: " + paymentId + " | Amount: ₹" + amount + " | Status: " + status; }
}

// Shipment Class
class Shipment {
    private int shipmentId;
    private String status;
    private String date;

    public Shipment(int shipmentId) { this.shipmentId = shipmentId; this.status = "Pending"; }
    public void shipOrder() { this.status = "Shipped"; this.date = java.time.LocalDate.now().toString(); }
    public String getStatus() { return status; }
    @Override
    public String toString() { return "Shipment ID: " + shipmentId + " | Status: " + status + " | Date: " + date; }
}

// ReturnRequest Class
class ReturnRequest {
    private int requestId;
    private OrderItem orderItem;
    private int quantity;
    private String reason;
    private boolean approved;

    public ReturnRequest(int requestId, OrderItem orderItem, int quantity, String reason) {
        this.requestId = requestId;
        this.orderItem = orderItem;
        this.quantity = quantity;
        this.reason = reason;
    }

    public void approveReturn() { this.approved = true; orderItem.getProduct().increaseStock(quantity); }
    public void rejectReturn() { this.approved = false; }
    @Override
    public String toString() { return "Return ID: " + requestId + " | Product: " + orderItem.getProduct().getName() + " | Qty: " + quantity + " | Approved: " + approved; }
}

// Main Application Class
public class ECommerceApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Product> products = new ArrayList<>();
        List<Customer> customers = new ArrayList<>();
        List<Order> orders = new ArrayList<>();

        int choice;
        do {
            System.out.println("\n=== E-Commerce System ===");
            System.out.println("1. Add Product\n2. Add Customer\n3. Place Order\n4. Display Products\n5. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Product ID, Name, Price, Stock: ");
                    products.add(new Product(sc.nextInt(), sc.next(), sc.nextDouble(), sc.nextInt()));
                    System.out.println("Product added!");
                }
                case 2 -> {
                    System.out.print("Enter Customer ID, Name, Email: ");
                    customers.add(new Customer(sc.nextInt(), sc.next(), sc.next()));
                    System.out.println("Customer added!");
                }
                case 3 -> {
                    if (products.isEmpty() || customers.isEmpty()) { System.out.println("Add products and customers first!"); break; }
                    System.out.print("Enter Customer ID: ");
                    int cid = sc.nextInt();
                    Customer c = customers.stream().filter(cust -> cust.getCustomerId() == cid).findFirst().orElse(null);
                    if (c == null) { System.out.println("Customer not found!"); break; }

                    List<OrderItem> items = new ArrayList<>();
                    System.out.print("How many items? ");
                    int n = sc.nextInt();
                    for (int i = 0; i < n; i++) {
                        System.out.print("Enter Product ID and Quantity: ");
                        int pid = sc.nextInt();
                        int qty = sc.nextInt();
                        Product p = products.stream().filter(prod -> prod.getProductId() == pid).findFirst().orElse(null);
                        if (p != null && p.getStockQuantity() >= qty) { items.add(new OrderItem(p, qty)); p.decreaseStock(qty); }
                        else { System.out.println("Invalid product or insufficient stock!"); }
                    }
                    Order o = new Order(orders.size() + 1, c);
                    items.forEach(o::addItem);
                    orders.add(o);
                    System.out.println("Order placed: " + o);
                }
                case 4 -> products.forEach(System.out::println);
                case 5 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice!");
            }
        } while (choice != 5);
        sc.close();
    }
}

