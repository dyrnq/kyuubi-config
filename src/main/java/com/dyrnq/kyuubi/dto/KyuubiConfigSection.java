package com.dyrnq.kyuubi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KyuubiConfigSection {
    private String name;
    private List<Map<String,String>> list;
}
