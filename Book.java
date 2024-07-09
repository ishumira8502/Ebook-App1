
package com.sla;
public class Book {
    private String title;
    private String author;
    private double price;
    private float version;
    public Book(String title, String author, double price, float version) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.version = version;

    }
    
    public void setTitle(String title) {
		this.title = title;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	public void setVersion(float version) {
		this.version = version;
	}
	

	// Getters and setters (optional for this example)
    public String getTitle() {
        return title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public double getPrice() {
        return price;
    }
    public float getVersion() {
        return version;
    }
    
    @Override
    public String toString() {
        return "Title: " + title + ", Author: " + author + ", Price: $" + price + ", Version: " + version + "";
    }
}
