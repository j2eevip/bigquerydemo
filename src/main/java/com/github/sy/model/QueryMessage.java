package com.github.sy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Sherlock
 * @since 2021/11/1-23:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class QueryMessage {
    private String tableName;
    private String csvDataRow;
}
