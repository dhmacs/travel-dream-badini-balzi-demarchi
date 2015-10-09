package travelDream.bean;

import travelDream.dto.CustomPackageDto;

/**
 * Created by federico on 29/01/14.
 */
public interface PackageValidatorInterface {

    public boolean isValid(CustomPackageDto customPackageDto);
}
