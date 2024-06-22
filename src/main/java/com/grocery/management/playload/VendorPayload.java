package com.grocery.management.playload;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VendorPayload {
    
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String location;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
