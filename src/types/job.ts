export interface Job {
  id: string;
  title: string;
  company: string;
  location: string;
  type: 'Full-time' | 'Part-time' | 'Contract' | 'Freelance';
  category: JobCategory;
  salary: {
    min: number;
    max: number;
    currency: string;
    period: 'hour' | 'day' | 'week' | 'month' | 'project';
  };
  description: string;
  requirements: string[];
  benefits: string[];
  skills: string[];
  postedAt: string;
  deadline: string;
  featured: boolean;
  urgent: boolean;
  slug: string;
}

export type JobCategory = 
  | 'data-entry'
  | 'typing'
  | 'captcha-typing'
  | 'form-filling'
  | 'sms-sending'
  | 'virtual-assistant'
  | 'customer-support'
  | 'content-writing';

export interface JobFilter {
  category?: JobCategory;
  type?: Job['type'];
  search?: string;
  minSalary?: number;
  maxSalary?: number;
}

export const JOB_CATEGORIES: { value: JobCategory; label: string; icon: string; description: string }[] = [
  { value: 'data-entry', label: 'Data Entry', icon: '📊', description: 'Enter and organize data into systems' },
  { value: 'typing', label: 'Typing Jobs', icon: '⌨️', description: 'Fast and accurate typing tasks' },
  { value: 'captcha-typing', label: 'Captcha Typing', icon: '🔐', description: 'Solve captchas and verification tasks' },
  { value: 'form-filling', label: 'Form Filling', icon: '📝', description: 'Complete online forms accurately' },
  { value: 'sms-sending', label: 'SMS Sending', icon: '📱', description: 'Send bulk SMS and messaging tasks' },
  { value: 'virtual-assistant', label: 'Virtual Assistant', icon: '💼', description: 'Administrative support remotely' },
  { value: 'customer-support', label: 'Customer Support', icon: '🎧', description: 'Help customers via chat or email' },
  { value: 'content-writing', label: 'Content Writing', icon: '✍️', description: 'Write articles and content' },
];
