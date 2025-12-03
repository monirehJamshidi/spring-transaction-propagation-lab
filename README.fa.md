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

