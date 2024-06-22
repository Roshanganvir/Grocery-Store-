package com.grocery.management.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.grocery.management.constants.ApplicationResponseCode;
import com.grocery.management.constants.Constants;
import com.grocery.management.constants.EntityTypes;
import com.grocery.management.dao.CategoryDao;
import com.grocery.management.dao.ItemDao;
import com.grocery.management.entity.Category;
import com.grocery.management.entity.Item;
import com.grocery.management.model.ResponseDetails;
import com.grocery.management.playload.ItemPayload;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import jakarta.transaction.Transactional;

@Service
@Configuration
@PropertySource("classpath:application.properties")
public class ItemService {

	@Autowired
	private Environment environment;

	@Autowired
	private ItemDao itemDao;

	@Autowired
	private CategoryDao categoryDao;

	@Transactional
	public ResponseEntity<Object> create(ItemPayload itemPayload) {

		Category category = (Category) categoryDao.getByName(itemPayload.getCategory());

		if (Objects.isNull(category)) {
			return ResponseEntity.badRequest()
					.body(new ResponseDetails(ApplicationResponseCode.RESOURCE_NOT_FOUND,
							MessageFormat.format(Constants.CATG_RESOURCE_NOT_FOUND, EntityTypes.CATEGORY.getTitle(),
									itemPayload.getName())));
		}

		if (!category.getItems().stream().map(String::toLowerCase).toList()
				.contains(itemPayload.getName().toLowerCase())) {
			return ResponseEntity.badRequest()
					.body(new ResponseDetails(ApplicationResponseCode.CONSTRAINT_VOILATION, MessageFormat.format(
							Constants.INVALID_CATG_WITH_ITEM, itemPayload.getName(), itemPayload.getCategory())));
		}

		if (itemDao.isAlreadyExit(itemPayload.getName(), itemPayload.getBrand())) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new ResponseDetails(ApplicationResponseCode.RESOURCE_CONFLICT,
							MessageFormat.format(Constants.ALREADY_EXITS_NAME_AND_BRAND, EntityTypes.ITEM.getTitle(),
									itemPayload.getName(), itemPayload.getBrand())));
		}

		if (Objects.isNull(itemPayload.getName()) || itemPayload.getName().equalsIgnoreCase("null")
				|| itemPayload.getName().isEmpty() || itemPayload.getName().trim().length() == 0) {
			return ResponseEntity.badRequest().body(new ResponseDetails(ApplicationResponseCode.CONSTRAINT_VOILATION,
					MessageFormat.format(Constants.RESOURCE_CANNOT_BE_EMPTY, "Name")));
		}

		if (itemPayload.getName().length() >= 20) {
			return ResponseEntity.badRequest().body(new ResponseDetails(ApplicationResponseCode.CONSTRAINT_VOILATION,
					MessageFormat.format(Constants.NAME_SIZE_SHOULD_BE, itemPayload.getName())));
		}

		if (Objects.isNull(itemPayload.getBrand()) || itemPayload.getBrand().equalsIgnoreCase("null")
				|| itemPayload.getBrand().isEmpty() || itemPayload.getBrand().trim().length() == 0) {
			return ResponseEntity.badRequest().body(new ResponseDetails(ApplicationResponseCode.CONSTRAINT_VOILATION,
					MessageFormat.format(Constants.RESOURCE_CANNOT_BE_EMPTY, "Brand")));
		}

		if (Objects.isNull(itemPayload.getCategory()) || itemPayload.getCategory().equalsIgnoreCase("null")
				|| itemPayload.getCategory().isEmpty() || itemPayload.getCategory().trim().length() == 0) {
			return ResponseEntity.badRequest().body(new ResponseDetails(ApplicationResponseCode.CONSTRAINT_VOILATION,
					MessageFormat.format(Constants.RESOURCE_CANNOT_BE_EMPTY, "Category")));
		}

		if (Objects.isNull(itemPayload.getDescription()) || itemPayload.getDescription().equalsIgnoreCase("null")
				|| itemPayload.getDescription().isEmpty() || itemPayload.getDescription().trim().length() == 0) {
			return ResponseEntity.badRequest().body(new ResponseDetails(ApplicationResponseCode.CONSTRAINT_VOILATION,
					MessageFormat.format(Constants.RESOURCE_CANNOT_BE_EMPTY, "Description")));
		}

		if (itemPayload.getQuntity() == 0) {
			return ResponseEntity.badRequest().body(new ResponseDetails(ApplicationResponseCode.CONSTRAINT_VOILATION,
					MessageFormat.format(Constants.QUNTITY_SIZE_NOT_ZERO, "Quntity")));
		}

		Item item = new Item();
		item.setId(EntityTypes.ITEM.getIdPrefix().concat(UUID.randomUUID().toString()));
		item.setName(itemPayload.getName());
		item.setBrand(itemPayload.getBrand());
		item.setCategory(itemPayload.getCategory());
		item.setDescription(itemPayload.getDescription());
		item.setMrp(itemPayload.getMrp());
		item.setPrice(itemPayload.getPrice());
		item.setQuntity(itemPayload.getQuntity());
		item.setCreatedAt(LocalDateTime.now());
		item.setUpdatedAt(LocalDateTime.now());
		item.setIsDeleted(false);

		item = (Item) itemDao.save(item);

		itemPayload.setId(item.getId());
		itemPayload.setName(item.getName());
		itemPayload.setBrand(item.getBrand());
		itemPayload.setCategory(item.getCategory());
		itemPayload.setDescription(item.getDescription());
		itemPayload.setMrp(item.getMrp());
		itemPayload.setPrice(item.getPrice());
		itemPayload.setQuntity(item.getQuntity());
		itemPayload.setCreatedAt(LocalDateTime.now());
		itemPayload.setUpdatedAt(LocalDateTime.now());

		return ResponseEntity.ok(itemPayload);
	}

	public ResponseEntity<Object> item(String id) {
		Item item = (Item) itemDao.getById(id);
		if (!Objects.isNull(item)) {
			ItemPayload itemPayload = new ItemPayload();
			itemPayload.setId(item.getId());
			itemPayload.setName(item.getName());
			itemPayload.setBrand(item.getBrand());
			itemPayload.setCategory(item.getCategory());
			itemPayload.setDescription(item.getDescription());
			itemPayload.setMrp(item.getMrp());
			itemPayload.setPrice(item.getPrice());
			itemPayload.setQuntity(item.getQuntity());
			itemPayload.setCreatedAt(item.getCreatedAt());
			itemPayload.setUpdatedAt(item.getUpdatedAt());
			return ResponseEntity.ok(itemPayload);
		}
		return ResponseEntity.badRequest().body(new ResponseDetails(ApplicationResponseCode.RESOURCE_NOT_FOUND,
				MessageFormat.format(Constants.RESOURCE_NOT_FOUND, EntityTypes.ITEM.getTitle(), id)));
	}

	public List<ItemPayload> items() {
		List<Item> items = itemDao.getAll();
		List<ItemPayload> itemPayloads = new ArrayList<>();
		for (Item item : items) {
			ItemPayload itemPayload = new ItemPayload();
			itemPayload.setId(item.getId());
			itemPayload.setName(item.getName());
			itemPayload.setBrand(item.getBrand());
			itemPayload.setCategory(item.getCategory());
			itemPayload.setDescription(item.getDescription());
			itemPayload.setMrp(item.getMrp());
			itemPayload.setPrice(item.getPrice());
			itemPayload.setQuntity(item.getQuntity());
			itemPayload.setCreatedAt(LocalDateTime.now());
			itemPayload.setUpdatedAt(LocalDateTime.now());
			itemPayloads.add(itemPayload);
		}
		return itemPayloads;
	}

	@Transactional
	public ResponseEntity<Object> update(ItemPayload itemPayload, String id) {
		Item item = (Item) itemDao.getById(id);
		System.out.println(item);
		Item newItem = new Item();
		if (!Objects.isNull(item)) {
			newItem.setId(id);
			newItem.setName(itemPayload.getName());
			newItem.setBrand(itemPayload.getBrand());
			newItem.setCategory(item.getCategory());
			newItem.setDescription(itemPayload.getDescription());
			newItem.setMrp(itemPayload.getMrp());
			newItem.setPrice(itemPayload.getPrice());
			newItem.setQuntity(itemPayload.getQuntity());
			newItem.setCreatedAt(item.getCreatedAt());
			newItem.setUpdatedAt(LocalDateTime.now());
			newItem.setIsDeleted(item.getIsDeleted());
			return ResponseEntity.ok(itemDao.save(newItem));
		}
		return ResponseEntity.badRequest().body(new ResponseDetails(ApplicationResponseCode.RESOURCE_NOT_FOUND,
				MessageFormat.format(Constants.RESOURCE_NOT_FOUND, EntityTypes.ITEM.getTitle(), id)));
	}

	@Transactional
	public ResponseEntity<Object> delete(String id) {
		Item item = (Item) itemDao.getById(id);
		if (!Objects.isNull(item)) {
			item.setIsDeleted(true);
			itemDao.save(item);
			return ResponseEntity.ok(itemDao.deleteById(id));
		}
		return ResponseEntity.badRequest().body(new ResponseDetails(ApplicationResponseCode.RESOURCE_NOT_FOUND,
				MessageFormat.format(Constants.RESOURCE_NOT_FOUND, EntityTypes.ITEM.getTitle(), id)));
	}

	public ResponseEntity<String> fileUpload(MultipartFile file, String category, String id) {
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body("Please select the file to upload");
		}

		try {

			String basePath = "D:\\GroceryManagment\\item";
			Path path = Paths.get(basePath, category, id, file.getOriginalFilename());
			Files.createDirectories(path.getParent());
			file.transferTo(path);
			return ResponseEntity.ok("File uploaded successfully");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Somthing went wrong try again");
	}

	public void genrateBill1() throws java.io.IOException {

		String filePath = environment.getRequiredProperty("paths.pdf");
		Path path = Paths.get(filePath);

		try {
			Files.createDirectories(path.getParent());

			PdfWriter pdfWriter = new PdfWriter(filePath);
			PdfDocument pdfDocument = new PdfDocument(pdfWriter);
			Document document = new Document(pdfDocument, PageSize.A4);

			List<Item> items = itemDao.getAll();

			addHeader(document);
			addTable(document, items);
			addFooter(document);

			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void addHeader(Document document) {

		document.add(
				new Paragraph("My Grocery Store").setFontSize(20).setTextAlignment(TextAlignment.CENTER).setBold());

		document.add(new Paragraph("Invoice").setFontSize(16).setTextAlignment(TextAlignment.CENTER).setBold());

	}

	private static void addTable(Document document, List<Item> items) {

		float[] columnWidths = { 1, 3, 2 };
		Table table = new Table(UnitValue.createPercentArray(columnWidths));
		table.setWidth(UnitValue.createPercentValue(100));

		table.addHeaderCell(createCell("Sr. No.", true));
		table.addHeaderCell(createCell("Item Name", true));
		table.addHeaderCell(createCell("Price", true));

		int i = 1;
		for (Item item : items) {
			table.addCell(createCell(String.valueOf(i++), false));
			table.addCell(createCell(item.getName(), false));
			table.addCell(createCell(item.getPrice().toString(), false));
		}

		document.add(table);

		double total = items.stream().mapToDouble(Item::getPriceAsDouble).sum();
		Table totalTable = new Table(UnitValue.createPercentArray(new float[] { 4, 1 }));
		totalTable.setWidth(UnitValue.createPercentValue(100));
		totalTable.addCell(createCell("Total:", true));
		totalTable.addCell(createCell(total + " INR", false));
		document.add(totalTable);
	}

	private static void addFooter(Document document) {
		document.add(new Paragraph("Contact: +918978986512").setFontSize(12).setTextAlignment(TextAlignment.CENTER));

		document.add(new Paragraph("Address:123, Sadar, Nagpur-440001").setFontSize(12)
				.setTextAlignment(TextAlignment.CENTER));

		document.add(
				new Paragraph("Thank you for your purchase!").setFontSize(12).setTextAlignment(TextAlignment.CENTER));

	}

	private static Cell createCell(String content, boolean isHeader) {
		Cell cell = new Cell().add(new Paragraph(content));
		if (isHeader) {
			cell.setBold();
			cell.setTextAlignment(TextAlignment.CENTER);
		} else {
			cell.setTextAlignment(TextAlignment.CENTER);
		}
		return cell;
	}

	public ResponseEntity<String> genrateBill() {
		try {
			genrateBill1();
			return ResponseEntity.ok("PDF generated successfully");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate PDF");
		}
	}

	@Transactional
	public List<ItemPayload> searchItems(String name, String description, String price, Long quantity) {
		List<Item> items = itemDao.getAll();

		List<Item> filteredItems = items.stream()
				.filter(item -> name == null || item.getName().toLowerCase().contains(name.toLowerCase()))
				.filter(item -> description == null
						|| item.getDescription().toLowerCase().contains(description.toLowerCase()))
				.filter(item -> price == null || item.getPrice().equalsIgnoreCase(price))
				.filter(item -> quantity == null || item.getQuntity().equals(quantity)).collect(Collectors.toList());

		return filteredItems.stream().map(this::convertToItemPayload).collect(Collectors.toList());
	}

	private ItemPayload convertToItemPayload(Item item) {
		ItemPayload payload = new ItemPayload();
		payload.setId(item.getId());
		payload.setName(item.getName());
		payload.setBrand(item.getBrand());
		payload.setCategory(item.getCategory());
		payload.setDescription(item.getDescription());
		payload.setMrp(item.getMrp());
		payload.setPrice(item.getPrice());
		payload.setQuntity(item.getQuntity());
		payload.setCreatedAt(item.getCreatedAt());
		payload.setUpdatedAt(item.getUpdatedAt());
		return payload;
	}
}
