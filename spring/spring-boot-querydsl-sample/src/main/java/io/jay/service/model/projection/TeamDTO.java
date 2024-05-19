package io.jay.service.model.projection;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TeamDTO {

    private Long id;

    private String name;

    private List<MemberDTO> memberDTOList;

    private MemberDTO memberDTO;


    public TeamDTO(Long id, String name, List<MemberDTO> memberDTOList) {
        this.id = id;
        this.name = name;
        this.memberDTOList = memberDTOList;
    }

    public TeamDTO(Long id, String name, MemberDTO memberDTO) {
        this.id = id;
        this.name = name;
        this.memberDTO = memberDTO;
    }

    public TeamDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}