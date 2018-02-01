# Wallet One P2P Android #
## Установка с помощью Gradle ##


```bash
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

Для интеграции P2P в ваш проект, используя Gradle, укажите его в списке зависимостей в build.gradle файле.
```bash
dependencies {
		compile 'com.github.WalletOne:P2PAndroid:0.2.2'
	}
```

```ruby
'0.1.2' # (Поддержка только банковских карт)
'0.2.2' #  (Поддержка разных платежных средств (Карты, Альфа-Клик, Qiwi и т.д.))
```


## Использование

> Рассмотрим в качестве примера фрилансинг площадку.

### Шаг 1 (Конфигурация модуля):

В Application class вашего app, в код OnCreate() метода, добавьте код инициализации приложения: 

```java
P2PCore.INSTANCE.setPlatform("PLATFORM_ID", "PLATFORM_SIGNATURE_KEY");
```

Значения `PLATFORM_ID` и `PLATFORM_SIGNATURE_KEY` вы получите при регистрации в сервисе [P2P Wallet One](https://www.walletone.com/ru/p2p/).

### Шаг 2 (Конфигурация пользователя):

После авторизации пользователя в приложении, необходимо записать в конфигурацию его данные.

Если пользователь выступает в роли заказчика:

```java
P2PCore.INSTANCE.setPayer("PLATFORM_USER_ID", "PLATFORM_USER_TITLE", "PLATFORM_USER_PHONE_NUMBER");
```
Если пользователь выступает в роли исполнителя:

```java
P2PCore.INSTANCE.setBeneficiary("PLATFORM_USER_ID", "PLATFORM_USER_TITLE", "PLATFORM_USER_PHONE_NUMBER");
```

Где:

`PLATFORM_USER_ID ` - Идентификатор пользователя в Вашей системе.

`PLATFORM_USER_TITLE ` - Имя пользователя в Вашей системе.

`PLATFORM_USER_PHONE_NUMBER ` - Номер телефона пользователя в Вашей системе.


### Шаг 3 (выбор способа оплаты исполнителя):

Если заказчиком во время создания заказа был указан метод расчета "Безопасная сделка ([P2P Wallet One](https://www.walletone.com/ru/p2p/))", то когда исполнитель подает заявку на исполнение заказа, он дожен добавить (выбрать) способ оплаты, на который будет зачислен платеж после выполнения.

```java
Intent intent = new Intent(getContext(), PaymentToolActivity.class);
intent.putExtra(ARG_OWNER_ID, Owner.BENEFICIARY);
intent.putExtra(ARG_SHOW_USE_NEW_PAYMENT_TOOL_LINK, true);
startActivityForResult(intent, REQUEST_SELECT_PAYMENT_TOOL);
```

После выбора карты, будет обработайте результат в `onActivityResult()` по ResultCode `REQUEST_SELECT_PAYMENT_TOOL`:

```java
@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
        case REQUEST_SELECT_PAYMENT_TOOL:
            if (resultCode == RESULT_OK) {
                Integer paymentToolId = data.getIntExtra(ARG_PAYMENT_TOOL_ID, 0);
		...
            }
            break;
    }
}                 
```

> В PaymentToolActivity имеется встроенная возможность добавить новый способ оплаты.

**Способ 2 (Создать собственную activity с добавлением, списком способов оплаты):**

Получение списка способов оплаты исполнителя:

```java
P2PCore.INSTANCE.beneficiariesPaymentTools.paymentTools(new CompleteHandler<PaymentToolsResult, Throwable>() {
    @Override
    public void completed(PaymentToolsResult result, Throwable error) {
      // result.getPaymentTools(): Список объектов PaymentTool. Будет null в случае ошибки запроса
      // error: Будет null в случае успешного запроса.
    }
});
```
---

Если у исполнителя нет добавленных раннее способов оплаты, то необходимо его добавить используя WebView. 

вы можете использовать готовый `LinkPaymentToolActivity`.

```java
Intent intent = new Intent(getContext(), LinkPaymentToolActivity.class);
startActivityForResult(intent, LinkPaymentToolActivity.REQUEST_LINK_PAYMENT_TOOL);
```

После того, как исполнитель добавит способ оплаты, обработайте результат в `onActivityResult()` по ResultCode `REQUEST_LINK_PAYMENT_TOOL`:

После добавления способа оплаты, Вам необходимо получить его `id`. Для этого воспользуйтесь методом получения списка способов оплаты (см. выше).

Еслы вы хотите сделать собственную Activity добавления способа оплаты, то для получения `RequestBuilder` используйте следующий код:

```java
final RequestBuilder request = P2PCore.INSTANCE.beneficiariesPaymentTools.addNewPaymentToolRequest("RETURN_HOST");

```

Где:

`RETURN_HOST` - URL на который произойдет переадресация, после завершения добавления.

> ВНИМАНИЕ! выбранный идентификатор платежного средства `paymentTool.id` Вам необходимо записать в заявку исполнителя к задаче. Идентификатор платежного средства исполнителя понадобится при принятии завки заказчиком.
### Шаг 4 (Оплата сделки - HOLD):

Когда заказчик выбрал оптимальное для него предложение исполнителя, необходимо создать сделку на стороне [P2P Wallet One](https://www.walletone.com/ru/p2p/) и оплатить ее (поставить средства в _HOLD_ на платежном средстве заказчика).

Для оплаты необходимо выбрать уже привязанное платежное средство или добавить новое.

выбор платежного средства:

**Способ 1 (Используя готовое решение из P2PUI):**

```java
    Intent intent = new Intent(getContext(), PaymentToolActivity.class);
    intent.putExtra(ARG_OWNER_ID, owner);
    intent.putExtra(ARG_SHOW_USE_NEW_PAYMENT_TOOL_LINK, true);
    startActivityForResult(intent, REQUEST_SELECT_PAYMENT_TOOL);
```

Если заказчик выбрал привязанную ранее карту, обработайте результат в `OnActivityResult()`

```java
 @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_SELECT_PAYMENT_TOOL:
                if (resultCode == RESULT_OK) {
                    Integer paymentTool = data.getIntExtra(ARG_PAYMENT_TOOL_ID, 0);
		    ...
                }
        }  
   }
                 
```

После выбора платежного средства необходимо создать сделку на стороне P2P Wallet One:

```java
P2PCore.INSTANCE.dealsManager.create(
	"PLATFORM_DEAL_ID",
	"PLATFORM_BENEFICIARY_ID",
	"PAYER_PAYMENT_TOOL_ID", // опциoнальный, 
	"BENEFICIARY_PAYMENT_TOOL_ID",
	100, // Сумма к оплате
	CurrencyId.RUB, // валюта
	"PLATFORM_DEAL_SHORT_DESCRIPTION",
	"PLATFORM_DEAL_FULL_DESCRIPTION",
	true,
	  new CompleteHandler<Deal, Throwable>() {
	      @Override
	      public void completed(Deal deal, Throwable error) {
		 if(deal != null){
		    // Pay deal
		 } else {
		    // Process error
		 }
	      }
	  }
)
```

Где:

`PLATFORM_DEAL_ID` - Идентификатор заявки/сделки в Вашей системе.

`PLATFORM_BENEFICIARY_ID` - Идентификатор исполнителя.

`PAYER_PAYMENT_TOOL_ID` - Идентификатор платежного средства заказчика

`BENEFICIARY_PAYMENT_TOOL_ID` - Идентификатор платежного средства исполнителя, записанный при создании заявки исполнителем.

`PLATFORM_DEAL_SHORT_DESCRIPTION` - Краткое описание сделки. Напимер "Создание сайта".

`PLATFORM_DEAL_FULL_DESCRIPTION ` - Полное описание сделки.

Для оплаты сделки можете использовать готовый Activity:

```java
Intent intent = new Intent(getContext(), PayDealActivity.class);
intent.putExtra(ARG_AUTH_DATA, "AUTH_DATA");
intent.putExtra(ARG_DEAL_ID, "DEAL_ID");
startActivityForResult(intent, REQUEST_PAY_DEAL);
```	
При оплате сделки имеется опциональный параметр ARG_AUTH_DATA. вы можете у пользователя предварительно запросить CVV выбраной карты и инициализировать контроллер оплаты вместе с CVV. В таком случае пользователю не будет предложено ввести _CVV_ на странице оплаты.

Если у пользователя нет привязанных платежных средств и он на странице списка платежных средств выберет "Использовать новый способ оплаты", то при создании сделки в параметр payerPaymentToolId установите значение null.

**Способ 2 (Использовать свою Activity с добавлением, списком карт):**

Получение списка платежных средств заказчика:

```java
P2PCore.INSTANCE.payersPaymentTools.paymentTools(new CompleteHandler<PaymentToolsResult, Throwable>() {
    @Override
    public void completed(PaymentToolsResult result, Throwable error) {
      // result: Ответ сервера с данными. Будет null в случае ошибки запроса. 
      // `result.getPaymentTools()` получает список платежных средств
      // error: Будет null в случае успешного запроса.
    }
});
```
---

Если у заказчика нет привязанных раннее платежных средств, то необходимо оплатить с нового используя WebView. 

Для получения `RequestBuilder` используйте следующий код:

```java
final RequestBuilder request = P2PCore.INSTANCE.dealsManager.payRequest("PLATFORM_DEAL_ID", true, "CVV/CVC", "RETURN_URL");
```

Где:

`PLATFORM_DEAL_ID` - Идентификатор заявки/сделки в Вашей системе
`CVV/CVC` - Если пользователь выбрал привязанную карту, вы можете нативно запросить у него _CVV/CVC_ карты. В таком случае, заказчику не будет предложено на странице оплаты вводить _CVC/CVV_.
`RETURN_URL` - URL на который произойдет переадресация, после завершения оплаты.

### Шаг 5 (Проверка оплаты):

После оплаты сделки, вы можете узнать статус оплаты используя следующий код:

```java
P2PCore.INSTANCE.dealsManager.status(dealId, new CompleteHandler<Deal, Throwable>() {
            @Override
            public void completed(Deal deal, Throwable error) {
                switch (deal.getDealStateId()) {
                    case DEAL_STATE_ID_PROCESSING:
		    	// Возникает в случае ошибки оплаты. Например недостаточно средств на карте заказчика
                        break;
                    case DEAL_STATE_ID_PAYMENT_PROCESS_ERROR:
		    	// В процессе оплаты. Тут необходимо проверить статус еще раз через некоторое время
                        break;
                    case DEAL_STATE_ID_PAID:
		       // Средства успешно зарезевированы
                        break;
                }
            }
        });
```

### Шаг 6 (Завершение сделки):

После выполнения работы фрилансером, ему необходимо перевести средства и завершить сделку.

Завершить сделку можно использую следующий код:

```java

P2PCore.INSTANCE.dealsManager.complete(dealId, new CompleteHandler<Deal, Throwable>() {
            @Override
            public void completed(Deal deal, Throwable error) {
	    	if(error != null){
			// Завершилось с ошибкой
		} else {
			switch (deal.getDealStateId()) {
			    case DEAL_STATE_ID_PAYOUT_PROCESSING:
				// выплата в процессе
				break;
			    case DEAL_STATE_ID_PAYOUT_PROCESS_ERROR:
				// Возникла ошибка во время выплаты. Например, карта исполнителя заблокирована
				break;
			    case DEAL_STATE_ID_COMPLETED:
				// выплата прошла успешно
				break;
			}
		}
                
            }
        });
```

Если Заказчик не удовлетворен работой, то он может отменить оплату:

```java
P2PCore.INSTANCE.dealsManager.cancel(request.getDealId(), new CompleteHandler<Deal, Throwable>() {
            @Override
            public void completed(Deal deal, Throwable error) {
		if(error != null){
			//process error
		} else {
			//process cancelation
		}
            }
        });
```

### Список (история) выплат исполнителю:

Получить список выплат исполнителю можно двумя способами:

**Способ 1 (Используя готовое решение из P2PUI):**

```java
Intent intent = new Intent(getContext(), PayoutsActivity.class);
intent.putExtra(ARG_DEAL_ID, "");
startActivity(intent);
```

Где `ARG_DEAL_ID` можно получить выплату по конкретной сделке.


**Способ 2 (Использовать свою Activity):**

```java
CompleteHandler<PayoutResult, Throwable> handler = new CompleteHandler<PayoutResult, Throwable>() {
            @Override
            public void completed(PayoutResult result, Throwable error) {
	        List<Payout> payoutsList = list.getPayouts();
		// payoutsList: - Список с объектами выплат
		// error: null в случае успешного запроса
            }
        };
P2PCore.INSTANCE.payoutsManager.payouts(pageNumber, itemsPerPage, dealId, handler);
```

### Список (история) возвратов заказчику:

Получить список возвратов заказчику можно двумя способами:

**Способ 1 (Используя готовое решение из P2PUI):**

```java
Intent intent = new Intent(getContext(), RefundsActivity.class);
intent.putExtra(ARG_DEAL_ID, "");
startActivity(intent);
```

Где `ARG_DEAL_ID` можно получить выплату по конкретной сделке.


**Способ 2 (Использовать свою Activity):**

```java
 CompleteHandler<RefundsResult, Throwable> handler = new CompleteHandler<RefundsResult, Throwable>() {
            @Override
            public void completed(RefundsResult list, Throwable error) {
                List<Refund> refundsList = list.getRefunds();
		// refundsList: - Список с объектами выплат
		// error: null в случае успешного запроса
            }
        };
P2PCore.INSTANCE.refundsManager.refunds(pageNumber, itemsPerPage, dealId, handler);

```

### Массовая оплата сделок:

В `P2PCore` имеется возможность массово завершить сделки.

```java
        // dealIds List<String> - список идентификаторов завершаемых сделок.
        P2PCore.INSTANCE.dealsManager.complete(dealIds, new CompleteHandler<List<Deal>, Throwable>() {
            @Override
            public void completed(List<Deal> deals, Throwable error) {
		            // deals: - Список с объектами сделок
		            // error: null в случае успешного запроса
            }
        });
```

Полный сценарий работы сервиса Вы можете посмотреть в проекте.
