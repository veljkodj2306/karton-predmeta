package com.fon.kartonpredmeta.service;


import com.fon.kartonpredmeta.dto.IshodDTO;
import com.fon.kartonpredmeta.entity.Ishod;
import com.fon.kartonpredmeta.exception.NotFoundException;
import com.fon.kartonpredmeta.mapper.IshodMapper;
import com.fon.kartonpredmeta.repository.IshodRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IshodService {

    private final IshodRepository ishodRepository;
    private final IshodMapper ishodMapper;

    public IshodService(IshodRepository ishodRepository, IshodMapper ishodMapper) {
        this.ishodRepository = ishodRepository;
        this.ishodMapper = ishodMapper;
    }

    public List<IshodDTO> getAllIshod() {

        return ishodRepository.findAll().stream().map(ishodMapper::toIshodDTO).toList();
    }

    public IshodDTO getIshodById(Long id) {
        Ishod ishod = ishodRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ishod sa ovim id ne postoji"));
        return ishodMapper.toIshodDTO(ishod);
    }

    public void deleteIshodById(Long id) {
        Ishod ishod = ishodRepository.findById(id).orElseThrow(() -> new NotFoundException("Ishod sa ovim id ne postoji"));
        ishodRepository.delete(ishod);
    }

    public IshodDTO saveIshod(IshodDTO ishodDTO) {
        Ishod ishod = ishodMapper.toEntity(ishodDTO);
        return ishodMapper.toIshodDTO(ishodRepository.save(ishod));
    }
}
