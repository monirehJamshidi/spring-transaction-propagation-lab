# spring transaction propagation lab


[🇬🇧 English](./README.md)

پروژه‌ای برای نمایش مدیریت تراکنش در Spring با انواع Propagation شامل REQUIRED، REQUIRES_NEW، MANDATORY، SUPPORTS، NEVER و NESTED.
در پروژه رابطهٔ Person → Car (به‌صورت OneToMany) پیاده‌سازی شده و با استفاده از CascadeType.ALL ذخیره‌سازی خودکار انجام می‌شود.
همچنین برای جلوگیری از خطای LazyInitializationException از تکنیک JOIN FETCH استفاده شده است.
در حالت عادی وقتی یک رکورد را حذف می‌کنیم:

✔ امکانات:

- مدیریت تراکنش پیشرفته با @Transactional
- پیاده‌سازی کامل Propagationهای مختلف
- رابطهٔ Person–Car با Cascade
- رفع خطای Lazy Loading با JOIN FETCH
- Repository اختصاصی مبتنی بر EntityManager
- مثال‌های واقعی: ذخیرهٔ لاگ، تراکنش تو در تو، اجبار به وجود تراکنش
- معماری تمیز و لایه‌ای (Entity, Repository, Service)

---

من توی کدم findAll را اول به اینصوت نوشتم :

    @Override 
    public List<Person> findAll() { 
        return entityManager
            .createQuery("select o from person o", Person.class)
            .getResultList(); 
    }

که به من خطای LazyInitializationException داد. که با سرج به این نکات رسیدم:

### 🔥 چرا این خطا رخ می‌دهد؟

در JPA، مجموعه‌های @OneToMany به‌صورت Lazy بارگذاری می‌شوند (پیش‌فرض).
یعنی:

- وقتی findAll() را اجرا می‌کنی، فقط Person لود می‌شود.
- وقتی حلقه می‌رسی به:


    person.getCarList()

می‌خواهد Carها را از دیتابیس Lazy لود کند.
اما!

⚠️ در آن لحظه تراکنش بسته شده و EntityManager دیگر فعال نیست!

    @Transactional(readOnly = true)
    public List<Person> findAll() {
        return repo.findAll();    // بعد از پایان متد، session بسته می‌شود!
    }

بنابراین Hibernate دیگر Session ندارد که مجموعه Lazy را initialize کند ⇒
LazyInitializationException

✅ راه‌حل 1 — Eager Loading (ساد‌ه‌ترین راه)

در Entity:

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL,
                orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Car> carList;

با EAGER کارها همیشه همراه Person لود می‌شوند و LazyException نمی‌گیری.
🔸 این ساده‌ترین راه است اما برای مجموعه‌های بزرگ مناسب نیست.

✅ راه‌حل 2 — استفاده از JOIN FETCH (راه‌حل حرفه‌ای‌تر)

بهترین روش برای پروژه‌های واقعی همین است.
در Repository:

    @Override
    public List<Person> findAll() {
        return entityManager.createQuery(
            "select distinct p from person p left join fetch p.carList",
            Person.class
        ).getResultList();
    }

این باعث می‌شود Car ها در همان query لود شوند ولی همچنان رابطه Lazy باقی بماند.

---

❗ رفتار Propagation.NEVER چیست؟
این Propagation می‌گوید:
### اگر حتی یک تراکنش فعال وجود داشته باشد → خطا بده!
این یعنی:

| Propagation  | اجازه داخل Transaction اصلی؟  |
| ------------ | ----------------------------- |
| REQUIRED     | ✔ بله                         |
| SUPPORTS     | ✔ اگر بود استفاده می‌کند      |
| MANDATORY    | ❌ اگر نبود خطا                |
| NEVER        | ❌ اگر بود خطا                 |
| REQUIRES_NEW | ✔ ولی با جدا کردن تراکنش جدید |
| NESTED       | ✔ ولی savepoint               |

### داخل یک تراکنش فعال (REQUIRED)

یک متد NEVER را صدا می‌زنی → پس Spring باید خطا بدهد.
این دقیقاً کاری است که Spring انجام می‌دهد:

    IllegalTransactionStateException:
    Existing transaction found for transaction marked with propagation 'never'

⚡ یعنی "تو گفتی اگر تراکنش هست اجرا نکن — منم اجرا نکردم!"

✅ اگر می‌خواهیم که خطا نگیرد باید:

🔵 راه ۱: متد NEVER را خارج از Transaction اصلی صدا بزن
یعنی SavePerson تراکنشی نباشد:

    public void SavePerson(Person person) {
        repo.save(person);
        helperService.doNeverWork(); // بدون تراکنش
    }

ولی این منطقی نیست چون کل پروژه‌ات بر اساس @Transactional نوشته شده.

🔵 راه ۲: NEVER را داخل متد جداگانه صدا بزن که خودش transactional نباشد

مثال:

    @Transactional
    public void SavePerson(Person person){

        repo.save(person);

        callNeverOutsideTx(); // خارج از تراکنش
    }

    public void callNeverOutsideTx() {
        helperService.doNeverWork(); // اینجا مجاز می‌شود
    }

چون callNeverOutsideTx تراکنش ندارد → NEVER بدون خطا اجرا می‌شود.

---

💡 پس رفتار Propagation.NEVER چیست و چرا مفید است؟
برای مواقعی که قطعاً نباید داخل تراکنش چیزی را ذخیره یا تغییر دهید.
مثلاً:

- logهای حساس
- عملیات read-only روی دیتابیس
- ارسال event
- auditهای خارج از تراکنش

---


وقتی از Propagation.NESTED استفاده کردم خطای ذیل بوجود آمد:


    NestedTransactionNotSupportedException:
    JpaDialect does not support savepoints


این خطا یعنی:
✔ دیتابیس یا درایور JPA شما "Savepoint" را پشتیبانی نمی‌کند

و بدون Savepoint، Hibernate نمی‌تواند Propagation.NESTED را اجرا کند.

💡 چرا NESTED نیاز به Savepoint دارد؟
Propagation.NESTED یعنی:
- داخل یک تراکنش فعال
- یک زیرتراکنش (savepoint) ساخته شود
- اگر nested fail شد، فقط تا savepoint rollback شود
- نه کل تراکنش اصلی

ولی اگر دیتابیس از savepoint پشتیبانی نکند → خطا.

❗ دیتابیس‌هایی که معمولاً Savepoint ندارند:

| دیتابیس             | NESTED پشتیبانی؟                         |
| ------------------- | ---------------------------------------- |
| **H2** (بعضی مودها) | ❌ بعضی مودها ندارد                       |
| **Oracle**          | ❌ از طریق JPA/Hibernate پشتیبانی نمی‌کند |
| **MySQL MyISAM**    | ❌ ندارد                                  |
| **MySQL InnoDB**    | ✔ دارد                                   |
| **PostgreSQL**      | ✔ کامل پشتیبانی می‌کند                   |


⭐ راه‌حل ۱: استفاده از RequiresNew به‌جای Nested

اگر فقط می‌خواهی زیرتراکنش مستقل داشته باشی:

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void doNestedWork() {
        auditRepository.save(...);
    }

بیشترین استفاده در پروژه‌های واقعی هم همین است.
Nested کمتر استفاده می‌شود چون محدودیت زیاد دارد.

⭐ راه‌حل ۲: اگر اصرار به NESTED داری → از PostgreSQL استفاده کن
PostgreSQL بهترین پشتیبانی از Savepoint دارد.

⭐ راه‌حل ۳: اگر H2 استفاده می‌کنی → تنظیم زیر را فعال کن:

    spring.datasource.url=jdbc:h2:mem:test;MODE=PostgreSQL

یا

    spring.jpa.properties.hibernate.connection.provider_disables_autocommit=true

ولی باز هم همیشه جواب نمی‌دهد.