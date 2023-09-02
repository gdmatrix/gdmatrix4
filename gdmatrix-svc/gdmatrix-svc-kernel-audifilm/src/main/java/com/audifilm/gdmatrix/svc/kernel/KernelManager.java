/*
 * GDMatrix
 *
 * Copyright (C) 2020-2023, Ajuntament de Sant Feliu de Llobregat
 *
 * This program is licensed and may be used, modified and redistributed under
 * the terms of the European Public License (EUPL), either version 1.1 or (at
 * your option) any later version as soon as they are approved by the European
 * Commission.
 *
 * Alternatively, you may redistribute and/or modify this program under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either  version 3 of the License, or (at your option)
 * any later version.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the licenses for the specific language governing permissions, limitations
 * and more details.
 *
 * You should have received a copy of the EUPL1.1 and the LGPLv3 licenses along
 * with this program; if not, you may find them at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * http://www.gnu.org/licenses/
 * and
 * https://www.gnu.org/licenses/lgpl.txt
 */
package com.audifilm.gdmatrix.svc.kernel;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;
import org.matrix.kernel.Address;
import org.matrix.kernel.AddressDocument;
import org.matrix.kernel.AddressDocumentFilter;
import org.matrix.kernel.AddressDocumentView;
import org.matrix.kernel.AddressFilter;
import org.matrix.kernel.AddressView;
import org.matrix.kernel.City;
import org.matrix.kernel.CityFilter;
import org.matrix.kernel.Contact;
import org.matrix.kernel.ContactFilter;
import org.matrix.kernel.ContactView;
import org.matrix.kernel.Country;
import org.matrix.kernel.CountryFilter;
import org.matrix.kernel.KernelList;
import org.matrix.kernel.KernelListItem;
import org.matrix.kernel.KernelMetaData;
import org.matrix.kernel.Person;
import org.matrix.kernel.PersonAddress;
import org.matrix.kernel.PersonAddressFilter;
import org.matrix.kernel.PersonAddressView;
import org.matrix.kernel.PersonDocument;
import org.matrix.kernel.PersonDocumentFilter;
import org.matrix.kernel.PersonDocumentView;
import org.matrix.kernel.PersonFilter;
import org.matrix.kernel.PersonPerson;
import org.matrix.kernel.PersonPersonFilter;
import org.matrix.kernel.PersonPersonView;
import org.matrix.kernel.PersonRepresentant;
import org.matrix.kernel.PersonRepresentantFilter;
import org.matrix.kernel.PersonRepresentantView;
import org.matrix.kernel.PersonView;
import org.matrix.kernel.Province;
import org.matrix.kernel.ProvinceFilter;
import org.matrix.kernel.Room;
import org.matrix.kernel.RoomFilter;
import org.matrix.kernel.RoomView;
import org.matrix.kernel.Street;
import org.matrix.kernel.StreetFilter;
import org.gdmatrix.security.User;

/**
 *
 * @author realor
 */
@RequestScoped
public class KernelManager
  implements org.gdmatrix.svc.kernel.KernelManager
{
  @PersistenceContext(name = "kernel_audifilm")
  EntityManager entityManager;

  @Inject
  User user;

  @Inject
  Setup setup;

  @Override
  public KernelMetaData getKernelMetaData()
  {
    KernelMetaData md = new KernelMetaData();

    int num = (int) (Math.random() * 0x1000000);
    setup.setColor("#" + Integer.toHexString(num));
    setup.save();

    System.out.println("url: " + setup.getUrl());
    System.out.println("color: " + setup.getColor());

//    try
//    {
//      {
//        KernelManagerRSClient client = new KernelManagerRSClient();
//        KernelManagerPort port = client.getKernelManagerPort("http://localhost:8080/api/");
//        System.out.println(">>> RS:" + port);
//        port.loadPerson("555");
//      }
//
//      {
//        KernelManagerWSClient client = new KernelManagerWSClient(
//          new URL("http://localhost:8080/services/kernel?wsdl"),
//          new QName("http://kernel.matrix.org/", "KernelManagerService"));
//        KernelManagerPort port = client.getKernelManagerPort();
//        System.out.println(">>> WS:" + port);
//        port.loadPerson("777");
//      }
//    }
//    catch (Exception ex)
//    {
//      System.out.println(ex);
//    }

    return md;
  }

  @Override
  public Person loadPerson(String personId)
  {
    System.out.println("EntityManager: " + entityManager);
    Query query = entityManager.createNativeQuery("select usrcod from org_usuari");
    List list = query.getResultList();
    System.out.println(list);

    if (user != null)
    {
      System.out.println("User: " + user.getUserId());
    }

    Person person = new Person();
    person.setPersonId(personId);
    person.setName("JUAN");
    person.setFirstSurname("GARCIA");
    person.setSecondSurname("LOPEZ");
    return person;
  }

  @Override
  public Person storePerson(Person person)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public boolean removePerson(String personId)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public int countPersons(PersonFilter filter)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public List<Person> findPersons(PersonFilter filter)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public List<PersonView> findPersonViews(PersonFilter filter)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Contact loadContact(String contactId)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Contact storeContact(Contact contact)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public boolean removeContact(String contactId)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public int countContacts(ContactFilter filter)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public List<ContactView> findContactViews(ContactFilter filter)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Address loadAddress(String addressId)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Address storeAddress(Address address)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public boolean removeAddress(String addressId)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public int countAddresses(AddressFilter filter)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public List<Address> findAddresses(AddressFilter filter)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public List<AddressView> findAddressViews(AddressFilter filter)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public PersonAddress loadPersonAddress(String personAddressId)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public PersonAddress storePersonAddress(PersonAddress personAddress)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public boolean removePersonAddress(String personAddressId)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public int countPersonAddresses(PersonAddressFilter filter)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public List<PersonAddressView> findPersonAddressViews(PersonAddressFilter filter)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public PersonRepresentant loadPersonRepresentant(String personRepresentantId)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public PersonRepresentant storePersonRepresentant(PersonRepresentant personRepresentant)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public boolean removePersonRepresentant(String personRepresentantId)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public List<PersonRepresentantView> findPersonRepresentantViews(PersonRepresentantFilter filter)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public PersonPerson loadPersonPerson(String personPersonId)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public PersonPerson storePersonPerson(PersonPerson personPerson)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public boolean removePersonPerson(String personPersonId)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public List<PersonPersonView> findPersonPersonViews(PersonPersonFilter filter)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public PersonDocument loadPersonDocument(String personDocId)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public PersonDocument storePersonDocument(PersonDocument personDocument)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public boolean removePersonDocument(String personDocId)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public List<PersonDocumentView> findPersonDocumentViews(PersonDocumentFilter filter)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public AddressDocument loadAddressDocument(String addressDocId)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public AddressDocument storeAddressDocument(AddressDocument addressDocument)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public boolean removeAddressDocument(String addressDocId)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public List<AddressDocumentView> findAddressDocumentViews(AddressDocumentFilter filter)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Room loadRoom(String roomId)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Room storeRoom(Room room)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public boolean removeRoom(String roomId)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public int countRooms(RoomFilter filter)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public List<Room> findRooms(RoomFilter filter)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public List<RoomView> findRoomViews(RoomFilter filter)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Country loadCountry(String countryId)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Country storeCountry(Country country)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public boolean removeCountry(String countryId)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public int countCountries(CountryFilter filter)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public List<Country> findCountries(CountryFilter filter)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Province loadProvince(String provinceId)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Province storeProvince(Province province)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public boolean removeProvince(String provinceId)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public int countProvinces(ProvinceFilter filter)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public List<Province> findProvinces(ProvinceFilter filter)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public City loadCity(String cityId)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public City storeCity(City city)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public boolean removeCity(String cityId)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public int countCities(CityFilter filter)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public List<City> findCities(CityFilter filter)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Street loadStreet(String streetId)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Street storeStreet(Street street)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public boolean removeStreet(String streetId)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public int countStreets(StreetFilter filter)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public List<Street> findStreets(StreetFilter filter)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public KernelListItem loadKernelListItem(KernelList list, String itemId)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public KernelListItem storeKernelListItem(KernelList list, KernelListItem item)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public boolean removeKernelListItem(KernelList list, String itemId)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public List<KernelListItem> listKernelListItems(KernelList list)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

}
