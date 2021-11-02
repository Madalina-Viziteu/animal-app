package com.sda.project.service;

import com.sda.project.controller.exception.ResourceNotFoundException;
import com.sda.project.dto.PetDto;
import com.sda.project.mapper.PetMapper;
import com.sda.project.model.Pet;
import com.sda.project.repository.PetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetService {

    private static final Logger log = LoggerFactory.getLogger(PetService.class);

    private final PetRepository petRepository;
    private final PetMapper petMapper;

    @Autowired
    public PetService(PetRepository petRepository, PetMapper petMapper) {
        this.petRepository = petRepository;
        this.petMapper = petMapper;
    }

    public PetDto save(PetDto petDto) {
        // TODO: use lambda
        Pet pet = petMapper.map(petDto);
        Pet savedPet = petRepository.save(pet);
        PetDto savedDto = petMapper.map(savedPet);
        return savedDto;
    }

    public List<PetDto> findAll() {
        return petRepository.findAll().stream()
                .map(pet -> petMapper.map(pet))
                .collect(Collectors.toList());
    }

    public Pet findByName(String name) {
        return petRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("pet not found"));
    }

    public PetDto findById(Long id) {
        log.info("finding pet with id {}", id);

        // find by id from repo
        return petRepository.findById(id)
                // convert to dto
                .map(pet -> petMapper.map(pet))
                .orElseThrow(() -> new ResourceNotFoundException("pet not found"));
    }

    public List<Pet> findByCategory(String category) {
        return petRepository.findByCategory(category).get();
    }

    public void update(PetDto dto) {
        log.info("updating pet with id {} with data {}", dto.getId(), dto);

        // find entity by id
        petRepository.findById(dto.getId())
                // copy values from dto to entity
                .map(pet -> petMapper.update(pet, dto)) // transform pet to pet
                // save the updated pet
                .map(updatedPet -> petRepository.save(updatedPet))  // pet -> save pet
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("pet not found");
                });
    }

    public void update2(PetDto dto) {
        Pet petToUpdate = petRepository.findById(dto.getId())
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("pet not found");
                });
        Pet updatedPet = petMapper.update(petToUpdate, dto);
        petRepository.save(updatedPet);
    }

    public void delete(PetDto petDto) {
        petRepository.delete(petMapper.map(petDto));
    }
}
