# Системный анализ и машинное моделирование 

### Лабораторная работа №3-4

#### Вариант 2

![image lab 3-4](https://github.com/palina-krukovich/system-analysis/blob/master/images/lab34.png)

### Лaбораторная работа №5-6

#### Вариант 35

В СМО вида `М/М/2/1` поступают заявки двух видов. 
Заявка первого вида появляется на входе с вероятностью `р`, 
второго с вероятностью `(1-р)`. 
Заявка первого вида имеет более высокий приоритет и 
может вытеснить заявку второго вида из канала в очередь, 
если место в очереди свободно или из системы, если место занято. 
В случае, когда заявка первого вида застает систему в состоянии 
обслуживания заявок первого вида, то она ставится в очередь, 
если место ожидания свободно или занято заявкой второго вида 
(менее приоритетная заявка теряется). 
Найти относительные пропускные способности `Q1` и `Q2`.  
`m = 0.5, l = 0.9, р = 0.5`.
