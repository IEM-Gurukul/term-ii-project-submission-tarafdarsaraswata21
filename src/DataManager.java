package service;

import model.LibraryItem;
import model.Member;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class DataManager {

    private static final String ITEMS_FILE   = "library_items.ser";
    private static final String MEMBERS_FILE = "library_members.ser";

    private DataManager() {
    }

    // ── SAVE ──

    public static void saveItems(Map<String, LibraryItem> items) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ITEMS_FILE))) {
            oos.writeObject(items);
            System.out.println("Items saved successfully to " + ITEMS_FILE);
        } catch (IOException e) {
            System.out.println("Error saving items: " + e.getMessage());
        }
    }

    public static void saveMembers(Map<String, Member> members) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(MEMBERS_FILE))) {
            oos.writeObject(members);
            System.out.println("Members saved successfully to " + MEMBERS_FILE);
        } catch (IOException e) {
            System.out.println("Error saving members: " + e.getMessage());
        }
    }

    // ── LOAD ──

    @SuppressWarnings("unchecked")
    public static Map<String, LibraryItem> loadItems() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(ITEMS_FILE))) {
            Map<String, LibraryItem> items = (Map<String, LibraryItem>) ois.readObject();
            System.out.println("Items loaded successfully from " + ITEMS_FILE);
            return items;
        } catch (IOException e) {
            System.out.println("No saved items found. Starting fresh.");
            return new HashMap<>();
        } catch (ClassNotFoundException e) {
            System.out.println("Error reading items file: " + e.getMessage());
            return new HashMap<>();
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Member> loadMembers() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(MEMBERS_FILE))) {
            Map<String, Member> members = (Map<String, Member>) ois.readObject();
            System.out.println("Members loaded successfully from " + MEMBERS_FILE);
            return members;
        } catch (IOException e) {
            System.out.println("No saved members found. Starting fresh.");
            return new HashMap<>();
        } catch (ClassNotFoundException e) {
            System.out.println("Error reading members file: " + e.getMessage());
            return new HashMap<>();
        }
    }

    // ── SAVE ALL / LOAD ALL ──

    public static void saveAll(Map<String, LibraryItem> items, Map<String, Member> members) {
        saveItems(items);
        saveMembers(members);
    }

    public static void loadAll(LibraryService service) {
        Map<String, LibraryItem> loadedItems   = loadItems();
        Map<String, Member>      loadedMembers = loadMembers();
        service.getItems().putAll(loadedItems);
        service.getMembers().putAll(loadedMembers);
    }
}
