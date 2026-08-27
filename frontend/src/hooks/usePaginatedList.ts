import { useState, useCallback, useEffect, useRef } from 'react';
import { Page } from '@/api/types/pagination';

function usePaginatedList<T>(fetchPage: (page: number) => Promise<Page<T>>) {
  const [items, setItems] = useState<T[]>([]);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [isLoading, setIsLoading] = useState(false);

  const loadMore = useCallback(() => {
    if (!hasMore || isLoading) return;

    setIsLoading(true);
    fetchPage(page)
      .then(result => {
        setItems(prev => [...prev, ...result.content]);
        setHasMore(!result.last);
        setPage(prev => prev + 1);
      })
      .catch(err => console.error('Pagination error:', err))
      .finally(() => setIsLoading(false));
  }, [hasMore, isLoading, page, fetchPage]);

  const loadMoreRef = useRef(loadMore);
  useEffect(() => {
    loadMoreRef.current = loadMore;
  }, [loadMore]);

  useEffect(() => {
    loadMoreRef.current();
  }, []); // montage uniquement

  return { items, loadMore, hasMore, isLoading };
}

export default usePaginatedList;
