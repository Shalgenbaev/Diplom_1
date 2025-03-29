package praktikum;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BurgerTest {
    private Burger burger;

    @Mock
    private Bun bun;  // Мок-объект булочки
    @Mock
    private Ingredient sauceIngredient;  // Мок-объект ингредиента (соус)
    @Mock
    private Ingredient fillingIngredient1;  // Мок-объект ингредиента (начинка 1)
    @Mock
    private Ingredient fillingIngredient2;  // Мок-объект ингредиента (начинка 2)

    @Before
    public void setUp() {
        burger = new Burger();  // Инициализация нового бургера перед каждым тестом
    }

    /**
     * Проверяет, что метод setBuns корректно устанавливает булочку в бургер.
     * Ожидается, что после вызова setBuns(bun) поле bun в бургере будет содержать установленную булочку.
     */
    @Test
    public void setBuns_ShouldSetCorrectBun() {
        burger.setBuns(bun);
        assertEquals("Проверка установки булочки в бургер", bun, burger.bun);
    }

    /**
     * Проверяет, что добавление ингредиента увеличивает количество ингредиентов в бургере.
     * Ожидается, что после добавления одного ингредиента размер списка ingredients увеличится на 1.
     */
    @Test
    public void addIngredient_ShouldIncreaseIngredientsCount() {
        int initialCount = burger.ingredients.size();
        burger.addIngredient(sauceIngredient);
        assertEquals("Проверка увеличения количества ингредиентов",
                initialCount + 1, burger.ingredients.size());
    }

    /**
     * Проверяет, что добавленный ингредиент действительно содержится в бургере.
     * Ожидается, что список ingredients будет содержать добавленный ингредиент.
     */
    @Test
    public void addIngredient_ShouldAddCorrectIngredient() {
        burger.addIngredient(sauceIngredient);
        assertTrue("Проверка наличия добавленного ингредиента",
                burger.ingredients.contains(sauceIngredient));
    }

    /**
     * Проверяет, что удаление ингредиента уменьшает количество ингредиентов.
     * Ожидается, что после удаления одного ингредиента размер списка ingredients уменьшится на 1.
     */
    @Test
    public void removeIngredient_ShouldDecreaseIngredientsCount() {
        burger.addIngredient(sauceIngredient);
        int initialCount = burger.ingredients.size();
        burger.removeIngredient(0);
        assertEquals("Проверка уменьшения количества ингредиентов",
                initialCount - 1, burger.ingredients.size());
    }

    /**
     * Проверяет, что удаляется именно указанный ингредиент.
     * Ожидается, что после удаления ингредиента по индексу 0, список больше не будет содержать этот ингредиент.
     */
    @Test
    public void removeIngredient_ShouldRemoveCorrectIngredient() {
        burger.addIngredient(sauceIngredient);
        burger.addIngredient(fillingIngredient1);
        burger.removeIngredient(0);
        assertFalse("Проверка удаления конкретного ингредиента",
                burger.ingredients.contains(sauceIngredient));
    }

    /**
     * Проверяет корректность перемещения ингредиента на новую позицию.
     * Ожидается, что после перемещения ингредиент окажется на указанной позиции,
     * а остальные ингредиенты сохранят свои позиции.
     */
    @Test
    public void moveIngredient_ShouldChangeIngredientPosition() {
        burger.addIngredient(sauceIngredient);
        burger.addIngredient(fillingIngredient1);
        burger.addIngredient(fillingIngredient2);

        Ingredient ingredientToMove = burger.ingredients.get(1);
        burger.moveIngredient(1, 2);

        assertEquals("Проверка перемещения ингредиента на новую позицию",
                ingredientToMove, burger.ingredients.get(2));
    }

    /**
     * Проверяет правильность расчета общей стоимости бургера.
     * Ожидается, что цена будет равна: (цена булочки × 2) + сумма цен всех ингредиентов.
     * Тест проверяет корректность вычисления этой суммы.
     */
    @Test
    public void getPrice_ShouldReturnCorrectPrice() {
        when(bun.getPrice()).thenReturn(20F);
        when(sauceIngredient.getPrice()).thenReturn(10F);
        when(fillingIngredient1.getPrice()).thenReturn(2F);

        burger.setBuns(bun);
        burger.addIngredient(sauceIngredient);
        burger.addIngredient(fillingIngredient1);

        float expectedPrice = (20F * 2) + 10F + 2F;
        assertEquals("Проверка расчета общей стоимости бургера",
                expectedPrice, burger.getPrice(), 0.001);
    }

    /**
     * Проверяет, что в чеке содержится название булочки.
     * Ожидается наличие строки с названием булочки в формате "(==== name ====)".
     */
    @Test
    public void getReceipt_ShouldContainBunName() {
        when(bun.getName()).thenReturn("white bun");

        burger.setBuns(bun);
        String receipt = burger.getReceipt();

        assertTrue("Проверка наличия названия булочки в чеке",
                receipt.contains("(==== white bun ====)"));
    }

    /**
     * Проверяет, что в чеке корректно отображается информация об ингредиенте.
     * Ожидается строка в формате "= type name =" для каждого ингредиента.
     * Тест проверяет корректность формирования этой строки.
     */
    @Test
    public void getReceipt_ShouldContainIngredientInfo() {
        when(bun.getName()).thenReturn("white bun");
        when(sauceIngredient.getType()).thenReturn(IngredientType.SAUCE);
        when(sauceIngredient.getName()).thenReturn("chili sauce");

        burger.setBuns(bun);
        burger.addIngredient(sauceIngredient);
        String receipt = burger.getReceipt();

        assertTrue("Проверка информации об ингредиенте в чеке",
                receipt.contains("= sauce chili sauce ="));
    }

    /**
     * Проверяет, что в чеке корректно отображается итоговая цена.
     * Ожидается строка с общей стоимостью в формате "Price: X,XXXXXX".
     * Тест проверяет корректность отображения итоговой суммы.
     */
    @Test
    public void getReceipt_ShouldContainTotalPrice() {
        when(bun.getName()).thenReturn("white bun");
        when(bun.getPrice()).thenReturn(10F);
        when(sauceIngredient.getPrice()).thenReturn(5F);
        when(sauceIngredient.getType()).thenReturn(IngredientType.SAUCE);
        when(sauceIngredient.getName()).thenReturn("chili sauce");

        burger.setBuns(bun);
        burger.addIngredient(sauceIngredient);
        String receipt = burger.getReceipt();

        assertTrue("Проверка отображения итоговой цены в чеке",
                receipt.contains("Price: 25,000000"));
    }
}