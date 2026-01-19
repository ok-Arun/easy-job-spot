export const authFetch = (url: string, options: RequestInit = {}) => {
  const token = localStorage.getItem('token');

  return fetch(url, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      Authorization: token ? `Bearer ${token}` : '',
      ...options.headers,
    },
  });
};
