package junitmockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Resource: https://examples.javacodegeeks.com/core-java/junit/junit-mockito-when-thenreturn-example/
 * 
 * Examining the class:
 * Line 16: We are using @RunWith(MockitoJUnitRunner.class), so that our program will run with Mockito.
 * Line 23: @BeforeClass annotation of JUnit is used for initial setup. This will run before running any other test case.
 * Line 27: EmployeeDAO mock is being created at this line.
 * Line 30, 33: New Employee objects are being created
 * Line 36-39: stubbing is done so that when some condition is matched what needs to be done i.e. we are using the When with thenReturn of the Mockito framework. This is the main crux of the example. This is where we have used our core principal of our example.
 * Line 42,43: multiple calls to same method. We will see the side effects below.
 * Line 49: Chaining of stubs.
 * Run As -> Maven test
 */

@RunWith(MockitoJUnitRunner.class)
public class EmployeeTest {

	private static EmployeeDAO mockEmployeeDAO;
	private static Employee emp1;
	private static Employee emp2;

	@BeforeClass
	public static void init() {

		// set EmployeeDAO mock object
		mockEmployeeDAO = mock(EmployeeDAO.class);

		// create an Employee object
		emp1 = new Employee(1001L, "Vinod Kumar Kashyap", "A+", "Male", 2);

		// create another Employee object
		emp2 = new Employee(1002L, "Dhwani Kashyap", "A+", "Female", 5);

		// stubbing is done for test cases
		when(mockEmployeeDAO.getAll()).thenReturn(Arrays.asList(emp1, emp2));
		when(mockEmployeeDAO.getEmployee(1001L)).thenReturn(emp1);
		when(mockEmployeeDAO.addEmployee(emp2)).thenReturn(emp2.getEmployeeName());
		when(mockEmployeeDAO.updateEmployee(emp2)).thenReturn(emp2.getEmployeeName());

		// we are calling twice and see that always second call wins
		when(mockEmployeeDAO.deleteEmployee(1001L)).thenReturn("DELETED");
		when(mockEmployeeDAO.deleteEmployee(1001L)).thenReturn("REMOVED");

		/*
		 * when chaining is to be done. We can also use:
		 * when(mockEmployeeDAO.getAwards(1001L)).thenReturn(2,4,9);
		 */
		when(mockEmployeeDAO.getAwards(1001L)).thenReturn(2).thenReturn(4).thenReturn(9);

	}

	@Test
	public void getAllTest() {
		List<Employee> allEmployees = mockEmployeeDAO.getAll();

		assertNotNull(allEmployees);
		assertEquals(2, allEmployees.size());
	}

	@Test
	public void getEmployeeTest() {
		Long employeeId = 1001L;
		Employee employee = mockEmployeeDAO.getEmployee(employeeId);

		assertNotNull(employee);
		assertEquals(Long.valueOf("1001"), employee.getEmployeeId());
		assertEquals("Vinod Kumar Kashyap", employee.getEmployeeName());
		assertEquals("A+", employee.getBloodGroup());
		assertEquals("Male", employee.getGender());
	}

	@Test
	public void addEmployeeTest() {
		String employeeName = mockEmployeeDAO.addEmployee(emp2);

		assertNotNull(employeeName);
		assertEquals("Dhwani Kashyap", employeeName);
	}

	@Test
	public void updateEmployeeTest() {
		String employeeName = mockEmployeeDAO.updateEmployee(emp2);

		assertNotNull(employeeName);
		assertEquals("Dhwani Kashyap", employeeName);
	}

	@Test
	public void deleteEmployeeTest() {
		String status = mockEmployeeDAO.deleteEmployee(1001L);

		assertEquals("REMOVED", status);
	}

	@Test
	public void multipleCallsTest() {
		int awards = mockEmployeeDAO.getAwards(1001L);
		System.out.println(awards);

		awards = mockEmployeeDAO.getAwards(1001L);
		System.out.println(awards);

		awards = mockEmployeeDAO.getAwards(1001L);
		System.out.println(awards);
	}
}
