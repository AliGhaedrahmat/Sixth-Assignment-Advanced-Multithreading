# Theoretical Questions: Atomic Variables

### 1. What output do you get from the program? Why?

When you run this program, you'll see that the **atomicCounter** ends up at exactly 2,000,000, but the **normalCounter** usually ends up with a smaller number, like 1,500,000 or so. That’s because the normal integer increments aren’t thread-safe—when two threads try to update it simultaneously, some increments get lost. The `AtomicInteger` uses special atomic operations to prevent this problem, so it always counts correctly.

---

### 2. What is the purpose of AtomicInteger in this code?

`AtomicInteger` ensures that when multiple threads update the same number, the updates happen safely without interfering with each other. It guarantees that incrementing the counter is done atomically, meaning as one single, uninterruptible operation, avoiding bugs where increments might be missed.

---

### 3. What thread-safety guarantees does `atomicCounter.incrementAndGet()` provide?

The method `incrementAndGet()` guarantees that the increment operation happens atomically — no two threads can perform it at the same time and cause inconsistent values. It’s an all-or-nothing operation, so you never see a partially updated number.

---

### 4. When would using a lock be better than an atomic variable?

Locks are better when you need to perform multiple operations together that must be consistent, like checking a condition and updating multiple variables. Atomic variables are ideal for simple updates to a single value, but locks are needed when your logic involves multiple steps or variables that need to stay in sync.

---

### 5. Besides AtomicInteger, what other atomic types are there in Java?

The `java.util.concurrent.atomic` package includes:

- `AtomicLong` — for atomic operations on long values.
- `AtomicBoolean` — for atomic true/false flags.
- `AtomicReference` — for atomic updates to object references.
- `AtomicIntegerArray` and `AtomicReferenceArray` — for atomic operations on arrays.
- `AtomicStampedReference` and `AtomicMarkableReference` — for advanced atomic references that help solve concurrency issues like the ABA problem.

---

### Monte Carlo π Estimation — Answers

1. **Was the multi-threaded implementation always faster than the single-threaded one?**

No, not always. Sometimes the multi-threaded version was faster, but other times it wasn’t. It really depends on things like how the threads are set up, how many CPU cores your machine has, and how the work is split.

2. **If yes, why?**

When it was faster, it’s because the work got split across multiple threads that ran at the same time on different CPU cores. This way, the program could do many calculations in parallel instead of one after another, which speeds things up.

3. **If not, what factors caused this, and what can be done to improve performance?**

There are a few reasons why the multi-threaded version might be slower or only a little faster:

- Starting and managing threads takes some extra time, so if the tasks are too small, the overhead can cancel out the benefits.
- Threads might need to access shared data or resources, which can cause them to wait for each other, slowing everything down.
- If you have more threads than CPU cores, threads have to take turns running, causing extra switching that wastes time.
- Using a shared random number generator without proper handling can also cause delays.

To fix this and get better speed:

- Make sure each thread has enough work so that the overhead is worth it.
- Reduce how much threads have to wait for shared resources, maybe by giving each thread its own data or using better locks.
- Keep the number of threads close to the number of CPU cores.
- Use separate random number generators per thread or use thread-safe generators designed for concurrency.

---

