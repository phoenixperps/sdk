<?php

declare(strict_types=1);

namespace PhoenixPerps;

/**
 * One page of a list: its items (a plain array: end($page->data) works), whether more follow, the cursor to them, and
 * the list's other fields as they came (the orders list's history_complete in $extra).
 *
 * @template T
 */
final class Page
{
    /**
     * @param list<T> $data
     * @param (\Closure(string): (Page<T>|null))|null $next
     * @param array<string, mixed> $extra
     */
    public function __construct(
        public array $data,
        public readonly bool $hasMore,
        public readonly ?string $nextCursor,
        public readonly ?int $asOf,
        public readonly ?RawResponse $raw = null,
        private readonly ?\Closure $next = null,
        public readonly array $extra = [],
    ) {
    }

    /** @return Page<T>|null the next page, or null after the last */
    public function nextPage(): ?Page
    {
        if (!$this->hasMore || $this->nextCursor === null || $this->nextCursor === '' || $this->next === null) {
            return null;
        }
        return ($this->next)($this->nextCursor);
    }

    /** @return \Generator<int, T> every item, page after page (at most $maxItems when given) */
    public function autoPagingIterator(?int $maxItems = null): \Generator
    {
        $n = 0;
        $page = $this;
        while ($page !== null) {
            foreach ($page->data as $item) {
                if ($maxItems !== null && $maxItems > 0 && $n >= $maxItems) {
                    return;
                }
                $n++;
                yield $item;
            }
            if ($maxItems !== null && $maxItems > 0 && $n >= $maxItems) {
                return;
            }
            $page = $page->nextPage();
        }
    }
}
