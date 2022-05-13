package dev.vality.messages.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("author")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {

    @Id
    private String id;

    private String email;

    private String fullName;
}
