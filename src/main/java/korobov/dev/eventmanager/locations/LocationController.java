package korobov.dev.eventmanager.locations;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import korobov.dev.eventmanager.locations.LocationCreateDto;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private final static Logger log = LoggerFactory.getLogger(LocationController.class);

    private final LocationService locationService;
    private final LocationDtoMapper dtoMapper;

    public LocationController(
            LocationService locationService,
            LocationDtoMapper dtoMapper
    ) {
        this.locationService = locationService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping
    public ResponseEntity<List<LocationDto>> getAllLocations() {
        log.info("Get request for get all locations");
        List<Location> locationList = locationService.getAllLocations();
        return ResponseEntity.ok(locationList.stream().map(dtoMapper::toDto).toList());
    }

    @PostMapping
    public ResponseEntity<LocationDto> createLocation(
            @RequestBody @Valid LocationCreateDto createDto
    ) {
        log.info("POST /locations — create request: createDto={}", createDto);
        Location toSave = dtoMapper.fromCreateDto(createDto);
        var createdLocation = locationService.createLocation(toSave);
        LocationDto responseDto = dtoMapper.toDto(createdLocation);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDto);
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<LocationDto> deleteLocation(
            @PathVariable("locationId") Long locationId
    ) {
        log.info("Get request for delete location: locationId={}", locationId);
        var deletedLocation = locationService.deleteLocation(locationId);
        return ResponseEntity
                .status(204)
                .body(dtoMapper.toDto(deletedLocation));
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<LocationDto> getLocation(
            @PathVariable("locationId") Long locationId
    ) {
        log.info("Get request for get location: locationId={}", locationId);
        var foundLocation = locationService.getLocationById(locationId);
        return ResponseEntity.status(200)
                .body(dtoMapper.toDto(foundLocation));
    }

    @PutMapping("/{locationId}")
    public ResponseEntity<LocationDto> updateLocation(
            @PathVariable("locationId") Long locationId,
            @RequestBody @Valid LocationCreateDto updateDto
    ) {
        log.info("PUT /locations/{} — update request: updateDto={}",
                locationId, updateDto);
        Location toUpdate = dtoMapper.fromCreateDto(updateDto);
        var updatedLocation = locationService.updateLocation(toUpdate, locationId);
        LocationDto responseDto = dtoMapper.toDto(updatedLocation);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

}
