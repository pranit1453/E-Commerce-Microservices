Start
↓
Receive OrderRequest
↓
Extract productIds from request
↓
Call Product Service → getProductById(productIds)
↓
Create productMap (productId → ProductResponse)
↓
Prepare stockRequest (productId → quantity)
↓
Call Inventory Service → checkStock(stockRequest)
↓
All Products In Stock?
├── No → Throw Exception ("Stock not available") → End
└── Yes → Continue
↓
Call Inventory Service → reserveStock(stockRequest)
↓
TRY BLOCK START
↓
Create Order Entity
→ Set OrderStatus = CREATED
↓
Initialize grandTotal = 0
↓
Loop through each OrderRequestItem
↓
Get product from productMap
↓
Product Exists?
├── No → Throw Exception ("Product not found")
└── Yes → Continue
↓
Calculate totalPrice = price × quantity
↓
Add to grandTotal
↓
Create OrderItem
↓
Add to orderItems list
↓
Set orderItems to Order
↓
Set totalPrice (grandTotal)
↓
Save Order to DB
↓
Map OrderItems → OrderItemResponse
↓
Build OrderResponse
↓
Return Response
↓
TRY BLOCK END

CATCH BLOCK
↓
Call Inventory Service → releaseStock(stockRequest)
↓
Throw Exception
↓
End
