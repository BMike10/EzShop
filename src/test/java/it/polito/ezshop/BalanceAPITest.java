package it.polito.ezshop;

import it.polito.ezshop.data.*;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class BalanceAPITest {
    private final it.polito.ezshop.data.EZShop ezshop = new EZShop();
    private String username = "testUserProductApiEZShop";
    private String password = "password";
    private int createdUserId = -1;

    @Before
    public void init() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException, InvalidProductCodeException, InvalidProductIdException {
        User u = null;
        if((u=ezshop.login(username, password))==null) {
            createdUserId = ezshop.createUser(username, password, RoleEnum.Administrator.name());
        }else if(u.getRole().equals(RoleEnum.Cashier.name())) {
            username+="123456789101112";
            createdUserId = ezshop.createUser(username, password, RoleEnum.Administrator.name());
        }
        ezshop.logout();
    }
    @After
    public void after() throws InvalidUsernameException, InvalidPasswordException, InvalidUserIdException, UnauthorizedException {
        if(createdUserId > 0) {
            ezshop.login(username, password);
            ezshop.deleteUser(createdUserId);
        }
    }

    @Test
    public void testRecordBalanceUpdate() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException {

        assertThrows(UnauthorizedException.class, ()->{ezshop.recordBalanceUpdate(50);});
        // login
        ezshop.login(username, password);
        //TODO: LOGIN AS CASHIER
        //After login

        //Negative new balance
        AccountBook aB = ezshop.getAccountBook();
        aB.setBalance(100);
        assertFalse(ezshop.recordBalanceUpdate(-101));

        //Correct update
        assertTrue(ezshop.recordBalanceUpdate(99));

    }

    @Test
    public void testGetCreditsAndDebits() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException {

        assertThrows(UnauthorizedException.class, ()->{ezshop.getCreditsAndDebits(LocalDate.now().minusDays(3),LocalDate.now().plusDays(3));});
        // login
        ezshop.login(username, password);
        //TODO: LOGIN AS CASHIER
        //After login
        //Valid request
        List<BalanceOperation> list = new ArrayList<>();
        AccountBookClass aB = ezshop.getAccountBook();
        aB.setBalanceOperationMap(new HashMap<>());
        BalanceOperation bo1 = new BalanceOperationClass(1,"SALE",20,LocalDate.now(),"CREDIT");
        BalanceOperation bo2 = new BalanceOperationClass(2,"SALE",20,LocalDate.now().plusDays(2),"CREDIT");
        BalanceOperation bo3 = new BalanceOperationClass(3,"SALE",20,LocalDate.now().minusDays(2),"CREDIT");
        aB.addBalanceOperation(bo1);
        aB.addBalanceOperation(bo2);
        aB.addBalanceOperation(bo3);
        list.add(bo1);
        list.add(bo2);
        list.add(bo3);
        assertEquals(list,ezshop.getCreditsAndDebits(LocalDate.now().minusDays(3),LocalDate.now().plusDays(3)));

    }
}
