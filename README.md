This is an e-commerce backend system with a microservices architecture designed by myself.

The project has the following features:

1. Loosely coupled design, with services primarily relying on message queues for asynchronous communication, featuring strong fault tolerance. The distributed architecture provides robust scalability as well.
2. Focuses on performance and data consistency under high concurrency. A sophisticated Redis-based automatic cache management mechanism has been designed, with cache information adopting various event-triggered update mechanisms. Many caching logics are encapsulated in Lua scripts to ensure the atomicity of operations. This not only significantly reduces the pressure on the database during peak events such as deals but also strictly ensures data consistency.
3. Focuses on user experience, designing a high-performance shopping cart module based on Redis sessions. The shopping cart actions will be first stored in the session and then asynchronously written to the database. Simultaneously, a sophisticated data consistency assurance mechanism based on Redis sets has been designed, which can ensure data consistency without compromising performance.















