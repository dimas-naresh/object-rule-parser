package dimasnaresh.lib.objectruleparser.param;

import java.util.List;

public class ClientAddress {
    private String detailAdress;
    private List<Address> addresses;

    @SuppressWarnings("unused method. This method called by reflection")
    public String getDetailAdress() {
        return detailAdress;
    }

    public void setDetailAdress(String detailAdress) {
        this.detailAdress = detailAdress;
    }



    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    @SuppressWarnings("unused method. This method called by reflection")
    public boolean containAddressName(String addressName) {
        if (addresses != null) {
            for (Address address : this.addresses){
                if (address.getAddressName().equals(addressName))
                    return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unused method. This method called by reflection")
    public boolean containAddressNameAndCityName(String addressName, String cityName) {
        if (addresses != null) {
            for (Address address : this.addresses){
                if (address.getAddressName().equals(addressName) && address.getCity().getCityName().equals(cityName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
